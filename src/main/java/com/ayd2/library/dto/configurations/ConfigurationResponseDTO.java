package com.ayd2.library.dto.configurations;

import java.math.BigDecimal;

public record ConfigurationResponseDTO(
    String name,
    String logo,
    BigDecimal dailyRate,
    BigDecimal lateFee,
    BigDecimal lossFee,
    String phone
) {}
