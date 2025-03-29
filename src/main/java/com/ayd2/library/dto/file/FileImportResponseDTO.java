package com.ayd2.library.dto.file;

import java.util.List;

public record FileImportResponseDTO(
    Integer books,
    Integer students,
    Integer loans,
    Integer degrees,
    List<FileImportErrorDTO> errors
) {}
