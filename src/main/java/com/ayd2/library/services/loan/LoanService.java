package com.ayd2.library.services.loan;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.*;
import com.ayd2.library.dto.payment.CalculatePaymentRequestDTO;
import com.ayd2.library.dto.payment.CalculatePaymentResponseDTO;
import com.ayd2.library.exceptions.LoanLimitExceededException;
import com.ayd2.library.exceptions.NoAvailableCopiesException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.exceptions.StudentSanctionedException;
import com.ayd2.library.models.loan.Loan;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    LoanResponseDTO createLoan(NewLoanRequestDTO loanRequestDTO) throws NotFoundException, StudentSanctionedException,
            NoAvailableCopiesException, LoanLimitExceededException;

    LoanResponseDTO getLoanById(UUID id) throws NotFoundException;
    PagedResponseDTO<LoanResponseDTO> getAllLoans(Pageable pageable);
    CalculatePaymentResponseDTO calculatePayment(CalculatePaymentRequestDTO calculateRequest) throws ServiceException;
    CalculatePaymentResponseDTO calculatePayment(Loan loan, LocalDate paidDate, ConfigurationResponseDTO config) throws ServiceException;
    void deleteLoan(UUID id) throws NotFoundException;
}
