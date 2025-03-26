package com.ayd2.library.dto.configurations;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

public record UpdateConfigurationRequestDTO(
    @Size(max = 255, message = "Name must be less than 255 characters")
    String name,

    @Positive(message = "Daily rate must be positive")
    @Digits(integer = 10, fraction = 2, message = "Daily rate must have up to 10 digits and 2 decimal places")
    BigDecimal dailyRate,

    @Positive(message = "Late fee must be positive")
    @Digits(integer = 10, fraction = 2, message = "Late fee must have up to 10 digits and 2 decimal places")
    BigDecimal lateFee,

    @Positive(message = "Loss fee must be positive")
    @Digits(integer = 10, fraction = 2, message = "Loss fee must have up to 10 digits and 2 decimal places")
    BigDecimal lossFee,

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    String phone,
    
    MultipartFile imageFile
) {}
