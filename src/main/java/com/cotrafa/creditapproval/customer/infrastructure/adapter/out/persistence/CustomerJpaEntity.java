package com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence.IdentificationTypeJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customers_user_id", columnNames = "user_id"),
                @UniqueConstraint(name = "uk_customers_identification", columnNames = {"identification_type_id", "identification_number"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User association is mandatory")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_user"))
    private UserJpaEntity user;

    @NotNull(message = "Identification type is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identification_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_identification_type"))
    private IdentificationTypeJpaEntity identificationType;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Identification number is mandatory")
    @Size(min = 5, max = 20, message = "Identification number must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Identification number must be alphanumeric")
    @Column(name = "identification_number", nullable = false, length = 20)
    private String identificationNumber;

    @NotNull(message = "Base salary is mandatory")
    @DecimalMin(value = "0.0", message = "Base salary cannot be negative")
    @DecimalMax(value = "15000000.0", message = "Base salary exceeds the maximum allowed for this profile")
    @Digits(integer = 13, fraction = 2, message = "Salary format invalid")
    @Column(name = "base_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal baseSalary;
}