package com.ayd2.library.dto.degrees;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewDegreeRequestDTO(
    @NotNull(message = "Code is required")
    @Positive(message = "Code must be a positive number")
    @Min(value = 10, message = "Code must be a number with a minimum of 2 digits")
    Long code,
    
    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 255, message = "Name must be between 5 and 255 characters")
    String name
) {}