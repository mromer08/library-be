package com.ayd2.library.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.*;

public record NewPaymentRequestDTO(
    @NotNull(message = "Loan is required")
    UUID loanId,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal normalLoanDebt,

    @Positive(message = "Amount must be positive")
    BigDecimal overdueLoanDebt,

    @Positive(message = "Amount must be positive")
    BigDecimal sanctionDebt,

    @FutureOrPresent(message = "Paid date must be in the present or future")
    LocalDate paidDate
) {}
