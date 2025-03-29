package com.ayd2.library.dto.students;

import java.math.BigDecimal;
import java.util.UUID;

import com.ayd2.library.dto.users.UserDetailResponseDTO;
import com.ayd2.library.models.degree.Degree;

public record StudentDetailResponseDTO(
    UUID id,
    Boolean isSanctioned,
    BigDecimal debt,
    Long carnet,
    UserDetailResponseDTO user,
    Degree degree
) {}
