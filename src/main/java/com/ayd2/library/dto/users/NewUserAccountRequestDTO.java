package com.ayd2.library.dto.users;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record NewUserAccountRequestDTO(
    @Email(message = "Email is not valid")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    String name,
    @Digits(integer = 13, fraction = 0, message = "CUI must be exactly 13 digits")
    Long cui,
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate
) {}
