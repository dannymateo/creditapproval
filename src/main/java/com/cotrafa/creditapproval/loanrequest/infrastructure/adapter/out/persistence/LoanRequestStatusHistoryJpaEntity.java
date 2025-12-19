package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.out.persistence.LoanRequestStatusJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Loan request reference is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_request_id", nullable = false, updatable = false)
    private LoanRequestJpaEntity loanRequest;

    @NotNull(message = "Status reference is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_request_status_id", nullable = false, updatable = false)
    private LoanRequestStatusJpaEntity loanRequestStatus;

    @NotNull
    @PastOrPresent(message = "Assignment date cannot be in the future")
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Boolean current = false;

    @Size(max = 500, message = "Observation cannot exceed 500 characters")
    @Column(length = 500)
    private String observation;

    @PrePersist
    protected void onAssign() {
        if (this.assignedAt == null) {
            this.assignedAt = LocalDateTime.now();
        }
    }
}