package com.ayd2.library.dto.books;

import java.time.LocalDate;
import java.util.UUID;

public record BookResponseDTO(
    UUID id,
    String author,
    String title,
    LocalDate publicationDate,
    String imageUrl
) {}
