package com.ayd2.library.dto.users;

import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.models.user.Role;


public record UserResponseDTO(
    UUID id,
    String email,
    String name,
    Long cui,
    LocalDate birthDate,
    Role role,
    String imageUrl
) {}
