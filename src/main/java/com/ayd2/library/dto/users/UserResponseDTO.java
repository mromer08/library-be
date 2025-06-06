package com.ayd2.library.dto.users;

import java.util.UUID;

import com.ayd2.library.models.user.Role;


public record UserResponseDTO(
    UUID id,
    String email,
    String name,
    Role role,
    String imageUrl,
    Boolean isApproved
) {}
