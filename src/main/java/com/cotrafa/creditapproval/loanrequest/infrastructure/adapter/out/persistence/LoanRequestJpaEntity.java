package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence.CustomerJpaEntity;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence.LoanTypeJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "loan_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestJpaEntity extends Auditable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanTypeJpaEntity loanType;

    @OneToMany(mappedBy = "loanRequest", fetch = FetchType.LAZY)
    private List<LoanRequestStatusHistoryJpaEntity> statusHistory;

    @Column(nullable = false, precision = 15, scale = 2, columnDefinition = "DECIMAL(15,2) CHECK (amount > 0)")
    private BigDecimal amount;

    @Column(name = "annual_rate", nullable = false, columnDefinition = "FLOAT8 CHECK (annual_rate > 0)")
    private Double annualRate;

    @Column(name = "term_months", nullable = false, columnDefinition = "INTEGER CHECK (term_months > 0)")
    private Integer termMonths;

    public String getCurrentStatusName() {
        if (statusHistory == null || statusHistory.isEmpty()) {
            return "PENDIENTE";
        }
        return statusHistory.stream()
                .filter(h -> h.getCurrent() != null && h.getCurrent())
                .findFirst()
                .map(h -> h.getLoanRequestStatus().getName())
                .orElse("PENDIENTE");
    }
}