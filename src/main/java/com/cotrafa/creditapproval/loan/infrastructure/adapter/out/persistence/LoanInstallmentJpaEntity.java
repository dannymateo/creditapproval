package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loan_installments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanInstallmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Positive(message = "Installment number must be greater than zero")
    @Column(nullable = false)
    private Integer installmentNumber;

    @NotNull
    @PositiveOrZero(message = "Principal amount cannot be negative")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @NotNull
    @PositiveOrZero(message = "Interest amount cannot be negative")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal interestAmount;

    @NotNull
    @Positive(message = "Total installment amount must be greater than zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @PositiveOrZero(message = "Remaining balance cannot be negative")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingBalance;

    @NotNull
    @Column(nullable = false)
    private LocalDate dueDate;
}