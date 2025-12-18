package com.cotrafa.creditapproval.loanrequest.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LoanRequest {
    private UUID id;
    private UUID customerId;
    private UUID loanTypeId;
    private BigDecimal amount;
    private Double annualRate;
    private Integer termMonths;
    private LocalDateTime createdAt;
}