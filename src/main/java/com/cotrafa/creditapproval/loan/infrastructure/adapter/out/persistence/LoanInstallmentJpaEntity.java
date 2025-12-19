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

    @NotNull(message = "Installment number is mandatory")
    @Positive(message = "Installment number must be greater than zero")
    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber;

    @NotNull(message = "Principal amount is mandatory")
    @PositiveOrZero(message = "Principal amount cannot be negative")
    @Column(name = "principal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @NotNull(message = "Interest amount is mandatory")
    @PositiveOrZero(message = "Interest amount cannot be negative")
    @Column(name = "interest_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal interestAmount;

    @NotNull(message = "Total payment is mandatory")
    @Positive(message = "Total installment amount must be greater than zero")
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Remaining balance is mandatory")
    @PositiveOrZero(message = "Remaining balance cannot be negative")
    @Column(name = "remaining_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingBalance;

    @NotNull(message = "Due date is mandatory")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
}