package com.ayd2.library.dto.degrees;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

public record UpdateDegreeRequestDTO(
    @Digits(integer = 5, fraction = 0, message = "Code must be a number with a maximum of 5 digits")
    Long code,

    @Size(min = 5, max = 255, message = "Name must be between 5 and 255 characters")
    String name
) {}
