package com.ayd2.library.dto.books;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateBookRequestDTO(
    UUID authorId,
    UUID publisherId,

    @Size(max = 255, message = "Title must be less than 255 characters")
    String title,

    @PositiveOrZero(message = "Quantity must be a positive number or zero")
    Integer quantity,

    @PastOrPresent(message = "Publication date must be in the past or present")
    LocalDate publicationDate,

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive number")
    BigDecimal price,

    MultipartFile imageFile
) {}
