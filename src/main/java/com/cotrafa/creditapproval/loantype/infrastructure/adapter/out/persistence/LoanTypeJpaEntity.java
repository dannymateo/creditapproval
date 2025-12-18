package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "loan_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeJpaEntity extends Auditable {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @Column(name = "automatic_validation", nullable = false)
    private Boolean automaticValidation;

    @Column(nullable = false)
    private Boolean active;
}
