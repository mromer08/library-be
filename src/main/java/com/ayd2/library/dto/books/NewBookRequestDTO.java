package com.ayd2.library.dto.books;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record NewBookRequestDTO(
    @NotNull(message = "Author ID is required")
    UUID authorId,

    @NotNull(message = "Publisher ID is required")
    UUID publisherId,

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 to 255 characters")
    String title,

    @NotBlank(message = "Code is required")
    @Pattern(
        regexp = "^\\d{3}-[A-Z]{3}$",
        message = "Book code must be in the format 000-XXX"
    )
    String code,

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^978-?\\d{3}-?\\d{3}-?\\d{3}-?\\d{1}$", message = "ISBN must be in the format 978-XXX-XXX-XXX-X or 978XXXXXXXXXXXX")
    String isbn,
    

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be a positive number or zero")
    Integer quantity,

    @PastOrPresent(message = "Publication date must be in the past or present")
    LocalDate publicationDate,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive number")
    BigDecimal price,

    MultipartFile imageFile
) {}
