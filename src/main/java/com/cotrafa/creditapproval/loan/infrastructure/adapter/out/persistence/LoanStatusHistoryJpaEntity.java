package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanstatus.infrastructure.adapter.out.persistence.LoanStatusJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_statuses_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanStatusHistoryJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "Loan reference is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanJpaEntity loan;

    @NotNull(message = "Status reference is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_status_id", nullable = false)
    private LoanStatusJpaEntity loanStatus;

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