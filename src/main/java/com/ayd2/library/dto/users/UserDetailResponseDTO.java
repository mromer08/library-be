package com.ayd2.library.dto.users;

import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.models.user.Role;

public record UserDetailResponseDTO(
    UUID id,
    String email,
    String name,
    Role role,
    String imageUrl,
    Boolean isApproved,
    Boolean emailVerified,
    Long cui,
    LocalDate birthDate
) {}
