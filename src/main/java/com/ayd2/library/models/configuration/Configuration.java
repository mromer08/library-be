package com.ayd2.library.models.configuration;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String logo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lateFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lossFee;

    @Column(length = 20)
    private String phone;
}