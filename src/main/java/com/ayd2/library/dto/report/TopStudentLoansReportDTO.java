package com.ayd2.library.dto.report;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;

public record TopStudentLoansReportDTO(
    PagedResponseDTO<LoanResponseDTO> loans,
    StudentResponseDTO student
) {}
