package com.cotrafa.creditapproval.loan.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LoanStatusHistory {
    private UUID id;
    private UUID loanId;
    private UUID loanStatusId;
    private LocalDateTime assignedAt;
    private Boolean current;
    private String observation;
}