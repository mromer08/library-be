package com.ayd2.library.models.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.models.loan.Loan;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false, unique = true)
    private Loan loan;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_date", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate paidDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type", nullable = false, length = 20)
    private PayType payType;
}
