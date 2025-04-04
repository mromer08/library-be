package com.ayd2.library.services.report;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.dto.report.OverduePayStudentReportDTO;
import com.ayd2.library.dto.report.RevenueReportDTO;
import com.ayd2.library.dto.report.TopDegreeInLoansReportDTO;
import com.ayd2.library.dto.report.TopStudentLoansReportDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

public interface ReportService {
    PagedResponseDTO<LoanResponseDTO> findLoansByDueDate(LocalDate date, Pageable pageable);
    PagedResponseDTO<LoanResponseDTO> findOverdueLoans(Pageable pageable);
    RevenueReportDTO generateRevenueReport(LocalDate startDate, LocalDate endDate, Pageable pageable);
    TopDegreeInLoansReportDTO findTopDegreeInLoans(LocalDate startDate, LocalDate endDate, Pageable pageable);
    OverduePayStudentReportDTO findOverduePaymentsByStudent(Long studentCarnet, LocalDate startDate, LocalDate endDate, Pageable pageable);
    TopStudentLoansReportDTO findTopStudentInLoans(LocalDate startDate, LocalDate endDate, Pageable pageable);
    PagedResponseDTO<BookResponseDTO> findBooksCurrentlyBorrowedByStudent(Long studentCarnet, Pageable pageable);
    PagedResponseDTO<BookResponseDTO> findOutOfStockBooks(Pageable pageable);
    PagedResponseDTO<BookResponseDTO> findNeverBorrowedBooks(Pageable pageable);
    PagedResponseDTO<StudentResponseDTO> findSanctionedStudents(Pageable pageable);
}