package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Loan type name cannot be blank")
    @Size(max = 100, message = "Loan type name cannot exceed 100 characters")
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @NotNull(message = "Annual rate is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual rate must be greater than 0")
    @DecimalMax(value = "100.0", message = "Annual rate cannot exceed 100%")
    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @NotNull(message = "Automatic validation flag is mandatory")
    @Builder.Default
    @Column(name = "automatic_validation", nullable = false)
    private Boolean automaticValidation = false;

    @NotNull(message = "Active status is mandatory")
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}