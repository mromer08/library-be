package com.ayd2.library.dto.report;

import java.math.BigDecimal;

public record RevenueTotalReportDTO(
    BigDecimal totalRevenue,
    BigDecimal totalNormal,
    BigDecimal totalDebt,
    BigDecimal totalSanctioned
) {}
