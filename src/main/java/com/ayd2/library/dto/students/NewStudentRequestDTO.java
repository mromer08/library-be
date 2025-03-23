package com.ayd2.library.dto.students;

import java.util.UUID;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record NewStudentRequestDTO(
    @Digits(integer = 9, fraction = 0, message = "Academic record number must be exactly 9 digits")
    Long academicRecordNumber,
    @NotNull(message = "Career ID is required")
    UUID degreeId
) {}
