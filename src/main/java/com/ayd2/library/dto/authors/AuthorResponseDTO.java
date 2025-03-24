package com.ayd2.library.dto.authors;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
    UUID id,
    String name,
    String nationality,
    LocalDate birthDate
) {}