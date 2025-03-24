package com.ayd2.library.dto.publishers;

import jakarta.validation.constraints.NotBlank;

public record PublisherRequestDTO(@NotBlank(message = "Name is required") String name) {}
