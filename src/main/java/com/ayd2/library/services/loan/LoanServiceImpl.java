package com.ayd2.library.services.loan;

import com.ayd2.library.dto.loan.*;
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
import com.ayd2.library.mappers.generic.GenericPageMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new LoanLimitExceededException("Student has exceeded the loan limit" );
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
}

