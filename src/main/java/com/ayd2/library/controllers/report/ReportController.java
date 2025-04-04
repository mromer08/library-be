package com.ayd2.library.controllers.report;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.dto.report.*;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.services.report.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/due-loans")
    public ResponseEntity<PagedResponseDTO<LoanResponseDTO>> getLoansByDueDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findLoansByDueDate(date, pageable));
    }


    @GetMapping("/overdue-loans")
    public ResponseEntity<PagedResponseDTO<LoanResponseDTO>> getOverdueLoans(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findOverdueLoans(pageable));
    }


    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportDTO> getRevenueReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.generateRevenueReport(startDate, endDate, pageable));
    }


    @GetMapping("/top-degree-loans")
    public ResponseEntity<TopDegreeInLoansReportDTO> getTopDegreeInLoans(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findTopDegreeInLoans(startDate, endDate, pageable));
    }


    @GetMapping("/student/{studentId}/overdue-payments")
    public ResponseEntity<OverduePayStudentReportDTO> getOverduePaymentsByStudent(
            @PathVariable Long studentCarnet,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findOverduePaymentsByStudent(studentCarnet, startDate, endDate, pageable));
    }


    @GetMapping("/top-student-loans")
    public ResponseEntity<TopStudentLoansReportDTO> getTopStudentInLoans(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findTopStudentInLoans(startDate, endDate, pageable));
    }


    @GetMapping("/student/{studentId}/borrowed-books")
    public ResponseEntity<PagedResponseDTO<BookResponseDTO>> getBooksBorrowedByStudent(
            @PathVariable Long studentCarnet,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findBooksCurrentlyBorrowedByStudent(studentCarnet, pageable));
    }


    @GetMapping("/out-of-stock-books")
    public ResponseEntity<PagedResponseDTO<BookResponseDTO>> getOutOfStockBooks(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findOutOfStockBooks(pageable));
    }


    @GetMapping("/never-borrowed-books")
    public ResponseEntity<PagedResponseDTO<BookResponseDTO>> getNeverBorrowedBooks(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findNeverBorrowedBooks(pageable));
    }


    @GetMapping("/sanctioned-students")
    public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> getSanctionedStudents(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reportService.findSanctionedStudents(pageable));
    }
}
