package com.ayd2.library.mappers.payment;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.payment.PaymentResponseDTO;
import com.ayd2.library.mappers.loan.LoanMapper;
import com.ayd2.library.models.payment.Payment;

@Mapper(componentModel = "spring", uses = {LoanMapper.class})
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentResponseDTO toPaymentResponseDTO(Payment payment);
}
