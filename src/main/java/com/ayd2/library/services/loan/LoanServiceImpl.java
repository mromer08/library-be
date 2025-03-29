package com.ayd2.library.services.loan;

import com.ayd2.library.dto.loan.*;
import com.ayd2.library.dto.file.FileImportErrorDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.loan.LoanMapper;
import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.file.EntityType;
import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.repositories.book.BookRepository;
import com.ayd2.library.repositories.loan.LoanRepository;
import com.ayd2.library.repositories.student.StudentRepository;
import com.ayd2.library.mappers.generic.GenericPageMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final LoanMapper loanMapper;
    private final GenericPageMapper loanPageMapper;

    @Override
    public LoanResponseDTO createLoan(NewLoanRequestDTO loanRequestDTO) throws NotFoundException, 
            StudentSanctionedException, NoAvailableCopiesException, LoanLimitExceededException {

        Student student = studentRepository.findByCarnet(loanRequestDTO.carnet())
                .orElseThrow(() -> new NotFoundException("Student not found with carnet: " + loanRequestDTO.carnet()));

        
                // boolean isSanctioned = loanRepository.existsByStudentAndReturnDateBefore(student, LocalDate.now().minusMonths(1));
                // if (isSanctioned) {
                //     throw new StudentSanctionedException("Student is sanctioned due to overdue loans.");
                // }

        Book book = bookRepository.findByCode(loanRequestDTO.bookCode())
                .orElseThrow(() -> new NotFoundException("Book not found with code: " + loanRequestDTO.bookCode()));

        if (book.getAvailableCopies() == 0) {
            throw new NoAvailableCopiesException("No available copies for book: " + loanRequestDTO.bookCode());
        }

        long activeLoans = loanRepository.countByStudentAndReturnDateIsNull(student);
        if (activeLoans >= 3) {
            throw new LoanLimitExceededException("Student has exceeded the loan limit (3 active loans)." );
        }


        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setLoanDate(loanRequestDTO.loanDate());
        loan.setDueDate(loanRequestDTO.loanDate().plusDays(3));

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toLoanResponseDTO(savedLoan);
    }

    // @Override
    // public LoanResponseDTO updateLoan(UUID id, UpdateLoanRequestDTO loanRequestDTO) throws NotFoundException {
    //     Loan loan = loanRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("Loan not found with id: " + id));

    //     if (loanRequestDTO.returnDate() != null) {
    //         Book book = loan.getBook();
    //         book.setAvailableCopies(book.getAvailableCopies() + 1);
    //         bookRepository.save(book);
    //     }

    //     Loan updatedLoan = loanRepository.save(loan);
    //     return loanMapper.toLoanResponseDTO(updatedLoan);
    // }

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
    public boolean createFromText(String text, EntityType entityType, List<FileImportErrorDTO> errors)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFromText'");
    }
}

