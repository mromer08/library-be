package com.ayd2.library.dto.authors;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UpdateAuthorRequestDTO(
    @Size(max = 255, message = "Name must be less than 255 characters")
    String name,

    @Size(max = 100, message = "Nationality must be less than 100 characters")
    String nationality,

    @Past(message = "Birth date must be in the past")
    LocalDate birthDate
) {}
