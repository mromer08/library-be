package com.ayd2.library.services.loan;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.*;
import com.ayd2.library.exceptions.LoanLimitExceededException;
import com.ayd2.library.exceptions.NoAvailableCopiesException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.exceptions.StudentSanctionedException;

import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    LoanResponseDTO createLoan(NewLoanRequestDTO loanRequestDTO) throws NotFoundException, StudentSanctionedException,
            NoAvailableCopiesException, LoanLimitExceededException;

    // LoanResponseDTO updateLoan(UUID id, UpdateLoanRequestDTO loanRequestDTO) throws NotFoundException;

    LoanResponseDTO getLoanById(UUID id) throws NotFoundException;

    PagedResponseDTO<LoanResponseDTO> getAllLoans(Pageable pageable);

    void deleteLoan(UUID id) throws NotFoundException;
}
