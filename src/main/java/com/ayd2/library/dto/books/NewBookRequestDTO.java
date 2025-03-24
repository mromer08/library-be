package com.ayd2.library.dto.books;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NewBookRequestDTO(
    @NotNull(message = "Author ID is required")
    UUID authorId,

    @NotNull(message = "Publisher ID is required")
    UUID publisherId,

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    String title,

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must be less than 20 characters")
    String code,

    @NotBlank(message = "ISBN is required")
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
