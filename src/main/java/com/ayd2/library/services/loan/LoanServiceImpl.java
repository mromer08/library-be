package com.ayd2.library.services.loan;

import com.ayd2.library.dto.loan.*;
import com.ayd2.library.dto.payment.CalculatePaymentRequestDTO;
import com.ayd2.library.dto.payment.CalculatePaymentResponseDTO;
import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.loan.LoanMapper;
import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.repositories.book.BookRepository;
import com.ayd2.library.repositories.loan.LoanRepository;
import com.ayd2.library.repositories.student.StudentRepository;
import com.ayd2.library.services.configuration.ConfigurationService;
import com.ayd2.library.specifications.loan.LoanSpecs;
import com.ayd2.library.mappers.generic.GenericPageMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final ConfigurationService configurationService;
    private final LoanMapper loanMapper;
    private final GenericPageMapper loanPageMapper;

    @Override
    public LoanResponseDTO createLoan(NewLoanRequestDTO loanRequestDTO) throws NotFoundException,
            StudentSanctionedException, NoAvailableCopiesException, LoanLimitExceededException {

        Student student = studentRepository.findByCarnet(loanRequestDTO.carnet())
                .orElseThrow(() -> new NotFoundException("Student not found with carnet: " + loanRequestDTO.carnet()));

        ConfigurationResponseDTO config = configurationService.getConfiguration();

        if (student.getIsSanctioned()) {
            throw new StudentSanctionedException("Student is sanctioned due to overdue loans.");
        }

        long activeLoans = loanRepository.countByStudentAndReturnDateIsNull(student);
        if (activeLoans >= config.maxLoans()) {
            throw new LoanLimitExceededException("Student has exceeded the loan limit");
        }
        Book book = bookRepository.findByCode(loanRequestDTO.bookCode())
                .orElseThrow(() -> new NotFoundException("Book not found with code: " + loanRequestDTO.bookCode()));

        if (book.getAvailableCopies() == 0) {
            throw new NoAvailableCopiesException("No available copies for book: " + loanRequestDTO.bookCode());
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setDebt(config.dailyRate());
        loan.setLoanDate(loanRequestDTO.loanDate());
        loan.setDueDate(loanRequestDTO.loanDate().plusDays(config.loanPeriodDays()));

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toLoanResponseDTO(savedLoan);
    }

    @Override
    public LoanResponseDTO getLoanById(UUID id) throws NotFoundException {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found with id: " + id));
        return loanMapper.toLoanResponseDTO(loan);
    }

    @Override
    public PagedResponseDTO<LoanResponseDTO> getAllLoans(Pageable pageable) {
        Page<Loan> page = loanRepository.findAll(pageable);
        Page<LoanResponseDTO> dtoPage = page.map(loanMapper::toLoanResponseDTO);
        return loanPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteLoan(UUID id) throws NotFoundException {
        if (!loanRepository.existsById(id)) {
            throw new NotFoundException("Loan not found with id: " + id);

        }
        loanRepository.deleteById(id);
    }

    @Override
    public CalculatePaymentResponseDTO calculatePayment(CalculatePaymentRequestDTO calculateRequest)
            throws ServiceException {
        ConfigurationResponseDTO config = configurationService.getConfiguration();

        Specification<Loan> loanSpec = Specification.where(LoanSpecs.hasStudentCarnet(calculateRequest.carnet()))
                .and(LoanSpecs.hasBookCode(calculateRequest.bookCode()))
                .and(LoanSpecs.isNotReturned());

        Loan loan = loanRepository.findOne(loanSpec).orElseThrow(
            () -> new NotFoundException("Loan not found with student carnet: " + calculateRequest.carnet() +
                    " and book code: " + calculateRequest.bookCode()));
        return calculatePayment(loan, calculateRequest.paidDate(), config);        
    }

    @Override
    public CalculatePaymentResponseDTO calculatePayment(Loan loan, LocalDate paidDate, ConfigurationResponseDTO config) throws ServiceException {
        paidDate = paidDate == null ? LocalDate.now() : paidDate;
        long totalLoanDays = ChronoUnit.DAYS.between(loan.getLoanDate(), paidDate);
        long normalLoanDays = ChronoUnit.DAYS.between(loan.getLoanDate(), loan.getDueDate());
    
        BigDecimal normalLoanDebt = BigDecimal.valueOf(Math.min(totalLoanDays, normalLoanDays))
                .multiply(config.dailyRate());
    
        BigDecimal overdueLoanDebt = BigDecimal.ZERO;
        BigDecimal sanctionDebt = BigDecimal.ZERO;
    
        if (paidDate.isAfter(loan.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), paidDate);
            overdueLoanDebt = BigDecimal.valueOf(overdueDays).multiply(config.lateFee());
        }

        if (paidDate.isAfter(loan.getLoanDate().plusDays(config.loanOverdueLimit()))) {
            sanctionDebt = loan.getBook().getPrice().add(config.lossFee());
            // Uncomment to apply the overdue limit to the sanction debt
            // overdueLoanDebt = BigDecimal.valueOf(config.loanOverdueLimit()).multiply(config.lateFee());

            Student student = loan.getStudent();
            if (!student.getIsSanctioned()) {
                student.setIsSanctioned(true);
                studentRepository.save(student);
            }
        }
    
        loan.setDebt(normalLoanDebt.add(overdueLoanDebt).add(sanctionDebt));
        Loan updatedLoan = loanRepository.save(loan);
    
        return new CalculatePaymentResponseDTO(
                loanMapper.toLoanResponseDTO(updatedLoan),
                normalLoanDebt,
                overdueLoanDebt,
                sanctionDebt);
    }
    
}
