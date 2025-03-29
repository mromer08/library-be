package com.ayd2.library.dto.file;

import com.ayd2.library.models.file.EntityType;
import com.ayd2.library.models.file.FieldType;

public record FileImportErrorDTO(
    Integer line,
    FieldType field,
    EntityType entity,
    String message
) {}
