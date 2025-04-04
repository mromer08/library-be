package com.ayd2.library.services.report;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.dto.report.OverduePayStudentReportDTO;
import com.ayd2.library.dto.report.RevenueReportDTO;
import com.ayd2.library.dto.report.TopDegreeInLoansReportDTO;
import com.ayd2.library.dto.report.TopStudentLoansReportDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ReportServiceImpl implements ReportService {
    
    @Override
    public PagedResponseDTO<LoanResponseDTO> findLoansByDueDate(LocalDate date, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findLoansByDueDate'");
    }

    @Override
    public PagedResponseDTO<LoanResponseDTO> findOverdueLoans(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOverdueLoans'");
    }

    @Override
    public RevenueReportDTO generateRevenueReport(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateRevenueReport'");
    }

    @Override
    public TopDegreeInLoansReportDTO findTopDegreeInLoans(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findTopDegreeInLoans'");
    }

    @Override
    public OverduePayStudentReportDTO findOverduePaymentsByStudent(Long studentCarnet, LocalDate startDate,
            LocalDate endDate, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOverduePaymentsByStudent'");
    }

    @Override
    public TopStudentLoansReportDTO findTopStudentInLoans(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findTopStudentInLoans'");
    }

    @Override
    public PagedResponseDTO<BookResponseDTO> findBooksCurrentlyBorrowedByStudent(Long studentCarnet,
            Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBooksCurrentlyBorrowedByStudent'");
    }

    @Override
    public PagedResponseDTO<BookResponseDTO> findOutOfStockBooks(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOutOfStockBooks'");
    }

    @Override
    public PagedResponseDTO<BookResponseDTO> findNeverBorrowedBooks(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findNeverBorrowedBooks'");
    }

    @Override
    public PagedResponseDTO<StudentResponseDTO> findSanctionedStudents(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findSanctionedStudents'");
    }
    

    
}
