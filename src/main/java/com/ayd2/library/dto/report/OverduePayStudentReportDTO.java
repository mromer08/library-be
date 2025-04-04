package com.ayd2.library.dto.report;

import java.math.BigDecimal;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;

public record OverduePayStudentReportDTO(
    PagedResponseDTO<LoanResponseDTO> loans,
    BigDecimal totalOverduePay
) {}

