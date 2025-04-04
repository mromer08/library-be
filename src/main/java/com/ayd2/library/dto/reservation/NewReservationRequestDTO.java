package com.ayd2.library.dto.reservation;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record NewReservationRequestDTO(
    @NotNull(message = "Book ID is required")
    UUID bookId
) {}
