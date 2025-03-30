package com.ayd2.library.controllers.payment;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.payment.CalculatePaymentRequestDTO;
import com.ayd2.library.dto.payment.CalculatePaymentResponseDTO;
import com.ayd2.library.dto.payment.PaymentResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.loan.LoanService;
import com.ayd2.library.services.payment.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    private final PaymentService paymentService;
    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<PaymentResponseDTO>> getAllPayments(@PageableDefault Pageable pageable) {
        PagedResponseDTO<PaymentResponseDTO> response = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody @Valid CalculatePaymentRequestDTO paymentRequestDTO)
            throws ServiceException {
        boolean paymentCreated = paymentService.createPayment(paymentRequestDTO);
        if (paymentCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/calculate")
    public ResponseEntity<CalculatePaymentResponseDTO> calculatePayment(
            @RequestParam String bookCode,
            @RequestParam Long carnet,
            @RequestParam LocalDate paidDate) throws ServiceException {

        CalculatePaymentRequestDTO requestDTO = new CalculatePaymentRequestDTO(bookCode, carnet, paidDate);
        CalculatePaymentResponseDTO responseDTO = loanService.calculatePayment(requestDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
