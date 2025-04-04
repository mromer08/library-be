package com.ayd2.library.dto.report;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;

public record RevenueReportDTO(
    PagedResponseDTO<LoanResponseDTO> loans,
    RevenueTotalReportDTO revenues
) {}
