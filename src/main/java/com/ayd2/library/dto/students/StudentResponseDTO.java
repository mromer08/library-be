package com.ayd2.library.dto.students;

import java.util.UUID;

public record StudentResponseDTO(
    UUID id,
    Boolean isSanctioned,
    Long carnet,
    String imageUrl,
    String name
) {}
