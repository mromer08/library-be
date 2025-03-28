package com.ayd2.library.dto.students;

import java.util.UUID;

public record StudentResponseDTO(
    UUID id,
    Boolean penalty,
    Long carnet,
    String imageUrl,
    String name
) {}
