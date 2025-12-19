package com.cotrafa.creditapproval.loan.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Loan {
    private UUID id;
    private UUID loanRequestId;
    private UUID customerId;
    private BigDecimal amount;
    private Double annualRate;
    private Integer termMonths;
    private LocalDate disbursementDate;
    private List<LoanInstallment> installments = new ArrayList<>();
}