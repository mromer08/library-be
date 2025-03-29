package com.ayd2.library.dto.books;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.PositiveOrZero;

public record BookSearchRequestDTO(
        String stringSearch,

        @PositiveOrZero(message = "minPrice must be positive or zero")
        BigDecimal minPrice,

        @PositiveOrZero(message = "maxPrice must be positive or zero")
        BigDecimal maxPrice,

        @PositiveOrZero(message = "minQuantity must be positive or zero")
        Integer minQuantity,

        @PositiveOrZero(message = "maxQuantity must be positive or zero")
        Integer maxQuantity,

        @PositiveOrZero(message = "minAvailableCopies must be positive or zero")
        Integer minAvailableCopies,

        @PositiveOrZero(message = "maxAvailableCopies must be positive or zero")
        Integer maxAvailableCopies,

        LocalDate publicationStartDate,
        LocalDate publicationEndDate,

        List<UUID> authorIds,
        List<UUID> publisherIds) {
}
