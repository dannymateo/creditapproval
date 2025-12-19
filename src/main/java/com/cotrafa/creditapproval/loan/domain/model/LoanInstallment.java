package com.cotrafa.creditapproval.loan.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LoanInstallment {
    private UUID id;
    private Integer installmentNumber;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private BigDecimal remainingBalance;
    private LocalDate dueDate;
}