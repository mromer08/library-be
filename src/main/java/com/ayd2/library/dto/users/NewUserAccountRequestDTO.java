package com.ayd2.library.dto.users;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record NewUserAccountRequestDTO(
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    String password,
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be at least 3 characters long")
    String name,

    @NotNull(message = "CUI is required")
    @Min(value = 1000000000000L, message = "CUI must be at least 13 digits")
    @Max(value = 9999999999999L, message = "CUI must be at most 13 digits")
    Long cui,
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate
) {}
