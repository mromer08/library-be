package com.ayd2.library.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record NewAuthorRequestDTO(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 to 255 characters")
    String name,

    @Size(min = 3, max = 100, message = "Nationality must be between 3 to 100 characters")
    String nationality,

    @Past(message = "Birth date must be in the past")
    LocalDate birthDate
) {}