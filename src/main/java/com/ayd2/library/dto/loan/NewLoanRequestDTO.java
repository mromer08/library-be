package com.ayd2.library.dto.loan;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record NewLoanRequestDTO(
    @NotNull(message = "Student ID cannot be null")
    Long carnet,

    @NotBlank(message = "Book code cannot be blank")
    @Pattern(
        regexp = "^\\d{3}-[A-Z]{3}$",
        message = "Book code must be in the format 000-XXX"
    )
    String bookCode,    

    @NotNull(message = "Loan date cannot be null")
    LocalDate loanDate
) {}
