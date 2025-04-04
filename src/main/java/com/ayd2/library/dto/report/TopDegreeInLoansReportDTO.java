package com.ayd2.library.dto.report;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.models.degree.Degree;

public record TopDegreeInLoansReportDTO(
    Degree degree,
    PagedResponseDTO<LoanResponseDTO> loans
) {}
