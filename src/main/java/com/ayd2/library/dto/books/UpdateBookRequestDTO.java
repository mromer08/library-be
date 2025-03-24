package com.ayd2.library.dto.books;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateBookRequestDTO(
    UUID authorId,
    UUID publisherId,

    @Size(max = 255, message = "Title must be less than 255 characters")
    String title,

    @Size(max = 20, message = "Code must be less than 20 characters")
    String code,

    @Size(max = 20, message = "ISBN must be less than 20 characters")
    String isbn,

    @PositiveOrZero(message = "Quantity must be a positive number or zero")
    int quantity,

    @PastOrPresent(message = "Publication date must be in the past or present")
    LocalDate publicationDate,

    @PositiveOrZero(message = "Available copies must be a positive number or zero")
    int availableCopies,

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive number")
    BigDecimal price,

    @Size(max = 255, message = "Image URL must be less than 255 characters")
    String imageUrl
) {}
