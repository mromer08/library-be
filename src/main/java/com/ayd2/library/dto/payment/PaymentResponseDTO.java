package com.ayd2.library.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.models.payment.PayType;

public record PaymentResponseDTO(
    UUID id,
    LoanResponseDTO loan,
    BigDecimal amount,
    LocalDate paidDate,
    PayType payType
) {}
