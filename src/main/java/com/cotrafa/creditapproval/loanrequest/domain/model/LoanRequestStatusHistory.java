package com.cotrafa.creditapproval.loanrequest.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LoanRequestStatusHistory {
    private UUID id;
    private UUID loanRequestId;
    private UUID loanRequestStatusId;
    private LocalDateTime assignedAt;
    private Boolean current;
    private String observation;
}