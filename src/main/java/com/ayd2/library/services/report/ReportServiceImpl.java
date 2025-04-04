package com.ayd2.library.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.dto.report.OverduePayStudentReportDTO;
import com.ayd2.library.dto.report.RevenueReportDTO;
import com.ayd2.library.dto.report.RevenueTotalReportDTO;
import com.ayd2.library.dto.report.TopDegreeInLoansReportDTO;
import com.ayd2.library.dto.report.TopStudentLoansReportDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.mappers.loan.LoanMapper;
import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.payment.PayType;
import com.ayd2.library.repositories.loan.LoanRepository;
import com.ayd2.library.repositories.payment.PaymentRepository;
import com.ayd2.library.specifications.loan.LoanSpecs;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final GenericPageMapper genericPageMapper;
    private final LoanMapper loanMapper;

    @Override
    public PagedResponseDTO<LoanResponseDTO> findLoansByDueDate(LocalDate date, Pageable pageable) {
        Page<Loan> page = loanRepository.findByDueDate(date, pageable);
        Page<LoanResponseDTO> dtoPage = page.map(loanMapper::toLoanResponseDTO);

        return genericPageMapper.toPagedResponse(dtoPage);

    }

    @Override
    public PagedResponseDTO<LoanResponseDTO> findOverdueLoans(Pageable pageable) {
        Specification<Loan> spec = Specification
                .where(LoanSpecs.dueDateBefore(LocalDate.now()))
                .and(LoanSpecs.isNotReturned());

        Page<Loan> page = loanRepository.findAll(spec, pageable);
        Page<LoanResponseDTO> dtoPage = page.map(loanMapper::toLoanResponseDTO);

        return genericPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public RevenueReportDTO generateRevenueReport(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // Obtener préstamos en el intervalo
        Specification<Loan> loanSpec = Specification
                .where(LoanSpecs.isReturned())
                .and(LoanSpecs.returnDateAfter(startDate))
                .and(LoanSpecs.returnDateBefore(endDate));
                

        Page<Loan> loanPage = loanRepository.findAll(loanSpec, pageable);
        Page<LoanResponseDTO> dtoPage = loanPage.map(loanMapper::toLoanResponseDTO);
        PagedResponseDTO<LoanResponseDTO> loans = genericPageMapper.toPagedResponse(dtoPage);

        // Obtener montos por tipo de pago
        BigDecimal total = paymentRepository.getTotalRevenueBetween(startDate, endDate);
        BigDecimal totalNormal = paymentRepository.getTotalByPayTypeBetween(PayType.NORMAL_LOAN, startDate, endDate);
        BigDecimal totalOverdue = paymentRepository.getTotalByPayTypeBetween(PayType.OVERDUE_LOAN, startDate, endDate);
        BigDecimal totalSanction = paymentRepository.getTotalByPayTypeBetween(PayType.SANCTION, startDate, endDate);

        RevenueTotalReportDTO totals = new RevenueTotalReportDTO(
                total != null ? total : BigDecimal.ZERO,
                totalNormal != null ? totalNormal : BigDecimal.ZERO,
                totalOverdue != null ? totalOverdue : BigDecimal.ZERO,
                totalSanction != null ? totalSanction : BigDecimal.ZERO);

        return new RevenueReportDTO(loans, totals);
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
