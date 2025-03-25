package com.ayd2.library.dto.books;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.models.author.Author;
import com.ayd2.library.models.publisher.Publisher;

public record BookResponseDTO(
    UUID id,
    Author author,
    Publisher publisher,
    String title,
    String isbn,
    String code,
    Integer quantity,
    Integer availableCopies,
    LocalDate publicationDate,
    BigDecimal price,
    String imageUrl
) {}
