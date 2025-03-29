package com.ayd2.library.dto.students;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewStudentRequestDTO(
    @NotNull(message = "Carnet is required")
    @Min(value = 100000000L, message = "Carnet must be at least 9 digits")
    @Max(value = 999999999L, message = "Carnet must be at most 9 digits")
    Long carnet,
    @NotNull(message = "Career ID is required")
    UUID degreeId
) {}
