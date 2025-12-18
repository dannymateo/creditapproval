package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence.CustomerJpaEntity;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence.LoanTypeJpaEntity;
import com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence.RoleJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestJpaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerJpaEntity customerId;

    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanTypeJpaEntity loanTypeId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}