package com.ayd2.library.dto.payment;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record CalculatePaymentRequestDTO(
    @NotBlank(message = "Code is required")
    @Pattern(
        regexp = "^\\d{3}-[A-Z]{3}$",
        message = "Book code must be in the format 000-XXX"
    )
    String bookCode,

    @NotNull(message = "Carnet is required")
    @Min(value = 100000000L, message = "Carnet must be at least 9 digits")
    // @Max(value = 999999999L, message = "Carnet must be at most 9 digits")
    Long carnet,

    @FutureOrPresent(message = "Paid date must be in the present or future")
    LocalDate paidDate
) {}
