package com.ayd2.library.dto.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;

public record LoanResponseDTO(
    UUID id,
    BookResponseDTO book,
    StudentResponseDTO student,
    LocalDate loanDate,
    LocalDate dueDate,
    LocalDate returnDate,
    BigDecimal debt
) {}
