package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_requests_statuses_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestStatusHistoryJpaEntity extends Auditable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID loanRequestId;

    @Column(nullable = false)
    private UUID loanRequestStatusId;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean current = false;

    @Column(length = 500)
    private String observation;

    @PrePersist
    protected void onAssign() {
        if (this.assignedAt == null) {
            this.assignedAt = LocalDateTime.now();
        }
    }
}