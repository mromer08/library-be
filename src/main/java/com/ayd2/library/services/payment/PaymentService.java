package com.ayd2.library.services.payment;

import org.springframework.data.domain.Pageable;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.payment.CalculatePaymentRequestDTO;
import com.ayd2.library.dto.payment.PaymentResponseDTO;
import com.ayd2.library.exceptions.ServiceException;

public interface PaymentService {
    boolean createPayment(CalculatePaymentRequestDTO paymentRequestDTO) throws ServiceException;
    PagedResponseDTO<PaymentResponseDTO> getAllPayments(Pageable pageable);
}
