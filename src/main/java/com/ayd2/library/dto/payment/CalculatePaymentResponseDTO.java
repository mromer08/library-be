package com.ayd2.library.dto.payment;

import java.math.BigDecimal;

import com.ayd2.library.dto.loan.LoanResponseDTO;

public record CalculatePaymentResponseDTO(
    LoanResponseDTO loan,
    BigDecimal normalLoanDebt,
    BigDecimal overdueLoanDebt,
    BigDecimal sanctionDebt
) {}
