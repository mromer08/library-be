package com.ayd2.library.repositories.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ayd2.library.models.payment.PayType;
import com.ayd2.library.models.payment.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment>{        
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.payType = :type AND p.paidDate BETWEEN :start AND :end")
    BigDecimal getTotalByPayTypeBetween(PayType type, LocalDate start, LocalDate end);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paidDate BETWEEN :start AND :end")
    BigDecimal getTotalRevenueBetween(LocalDate start, LocalDate end);
}
