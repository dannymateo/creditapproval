package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence.CustomerJpaEntity;
import com.cotrafa.creditapproval.loanrequeststatus.domain.constants.LoanRequestStatusConstants;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence.LoanTypeJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Customer is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerJpaEntity customer;

    @NotNull(message = "Loan type is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanTypeJpaEntity loanType;

    @OneToMany(mappedBy = "loanRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoanRequestStatusHistoryJpaEntity> statusHistory;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Amount format invalid")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Annual rate is mandatory")
    @Positive(message = "Annual rate must be greater than zero")
    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @NotNull(message = "Term months is mandatory")
    @Positive(message = "Term months must be greater than zero")
    @Min(value = 1, message = "Minimum term is 1 month")
    @Column(name = "term_months", nullable = false)
    private Integer termMonths;


    /**
     * Help to obtain the current state from the history.
     */
    public String getCurrentStatusName() {
        if (statusHistory == null || statusHistory.isEmpty()) {
            return LoanRequestStatusConstants.PENDING_REVIEW;
        }
        return statusHistory.stream()
                .filter(h -> h.getCurrent() != null && h.getCurrent())
                .findFirst()
                .map(h -> h.getLoanRequestStatus().getName())
                .orElse(LoanRequestStatusConstants.PENDING_REVIEW);
    }
}