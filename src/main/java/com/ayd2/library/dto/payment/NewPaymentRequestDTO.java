package com.ayd2.library.dto.payment;

import java.math.BigDecimal;
import java.util.UUID;

import com.ayd2.library.models.payment.PayType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewPaymentRequestDTO(
    @NotNull(message = "Loan is required")
    UUID loanId,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal amount,

    @NotNull(message = "Pay type is required")
    PayType payType
) {}
