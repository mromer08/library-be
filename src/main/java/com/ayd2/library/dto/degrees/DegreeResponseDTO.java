package com.ayd2.library.dto.degrees;

import java.util.UUID;

public record DegreeResponseDTO(
    UUID id,
    Long code,
    String name
) {}