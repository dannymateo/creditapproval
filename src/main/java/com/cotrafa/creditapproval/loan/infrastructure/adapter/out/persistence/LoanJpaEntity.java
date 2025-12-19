package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence.CustomerJpaEntity;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence.LoanRequestJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "loans", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"loan_request_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "loan_request_id", nullable = false, updatable = false)
    private UUID loanRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_request_id", referencedColumnName = "id", insertable = false, updatable = false)
    private LoanRequestJpaEntity loanRequest;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CustomerJpaEntity customer;

    @Column(nullable = false, precision = 19, scale = 2)
    @Positive(message = "The loan amount must be greater than zero")
    private BigDecimal amount;

    @Column(nullable = false)
    @Positive(message = "The loan annual rate must be greater than zero")
    private Double annualRate;

    @Column(nullable = false)
    @Positive(message = "The loan term months must be greater than zero")
    private Integer termMonths;

    @Column(nullable = false)
    private LocalDate disbursementDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private List<LoanInstallmentJpaEntity> installments;
}