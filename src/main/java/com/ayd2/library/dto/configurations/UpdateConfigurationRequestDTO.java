package com.ayd2.library.dto.configurations;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateConfigurationRequestDTO(
    // @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    String name,

    @Size(max = 255, message = "Logo URL must be less than 255 characters")
    String logo,

    // @NotNull(message = "Daily rate is required")
    @Positive(message = "Daily rate must be positive")
    @Digits(integer = 10, fraction = 2, message = "Daily rate must have up to 10 digits and 2 decimal places")
    BigDecimal dailyRate,

    // @NotNull(message = "Late fee is required")
    @Positive(message = "Late fee must be positive")
    @Digits(integer = 10, fraction = 2, message = "Late fee must have up to 10 digits and 2 decimal places")
    BigDecimal lateFee,

    // @NotNull(message = "Loss fee is required")
    @Positive(message = "Loss fee must be positive")
    @Digits(integer = 10, fraction = 2, message = "Loss fee must have up to 10 digits and 2 decimal places")
    BigDecimal lossFee,

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    String phone
) {}
