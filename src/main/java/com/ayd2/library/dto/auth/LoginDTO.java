package com.ayd2.library.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @Email(message = "Email is not valid")
    String email,
    @NotBlank(message = "Password is required")
    String password
) {}
