package com.ayd2.library.services.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.mappers.payment.PaymentMapper;
import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.payment.PayType;
import com.ayd2.library.models.payment.Payment;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.payment.CalculatePaymentRequestDTO;
import com.ayd2.library.dto.payment.CalculatePaymentResponseDTO;
import com.ayd2.library.dto.payment.PaymentResponseDTO;
import com.ayd2.library.repositories.book.BookRepository;
import com.ayd2.library.repositories.loan.LoanRepository;
import com.ayd2.library.repositories.payment.PaymentRepository;
import com.ayd2.library.repositories.reservation.ReservationRepository;
import com.ayd2.library.repositories.student.StudentRepository;
import com.ayd2.library.services.loan.LoanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PaymentServiceImpl implements PaymentService {
    private final static int RESERVATION_MAX_DAYS = 1;

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final PaymentMapper paymentMapper;
    private final GenericPageMapper paymentPageMapper;
    private final LoanService loanService;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public boolean createPayment(CalculatePaymentRequestDTO paymentRequestDTO) throws ServiceException {
        CalculatePaymentResponseDTO paymentResponseDTO = loanService.calculatePayment(paymentRequestDTO);
        Loan loan = loanRepository.findById(paymentResponseDTO.loan().id()).get();

        createPayment(loan, paymentResponseDTO.normalLoanDebt(), PayType.NORMAL_LOAN,
                paymentRequestDTO.paidDate());
        createPayment(loan, paymentResponseDTO.overdueLoanDebt(), PayType.OVERDUE_LOAN,
                paymentRequestDTO.paidDate());
        createPayment(loan, paymentResponseDTO.sanctionDebt(), PayType.SANCTION, paymentRequestDTO.paidDate());

        loan.setReturnDate(paymentRequestDTO.paidDate());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        reservationRepository.updateExpirationDateByBook(book, LocalDate.now().plusDays(RESERVATION_MAX_DAYS));
        return true;
    }

    @Override
    public PagedResponseDTO<PaymentResponseDTO> getAllPayments(Pageable pageable) {
        Page<Payment> page = paymentRepository.findAll(pageable);
        Page<PaymentResponseDTO> dtoPage = page.map(paymentMapper::toPaymentResponseDTO);
        return paymentPageMapper.toPagedResponse(dtoPage);
    }

    private boolean createPayment(Loan loan, BigDecimal amount, PayType payType, LocalDate paidDate)
            throws ServiceException {
        paidDate = paidDate != null ? paidDate : LocalDate.now();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        Student student = loan.getStudent();
        if (payType == PayType.SANCTION && student.getIsSanctioned()) {
            student.setIsSanctioned(false);
            studentRepository.save(student);
        }
        Payment payment = new Payment();
        payment.setLoan(loan);
        payment.setAmount(amount);
        payment.setPayType(payType);
        payment.setPaidDate(paidDate);
        paymentRepository.save(payment);

        return true;
    }
}
