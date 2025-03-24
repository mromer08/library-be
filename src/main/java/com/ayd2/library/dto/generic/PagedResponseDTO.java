package com.ayd2.library.dto.generic;

import java.util.List;

public record PagedResponseDTO<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {}