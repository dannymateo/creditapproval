package com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence.IdentificationTypeJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserJpaEntity;
import jakarta.persistence.*;
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
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_user"))
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identification_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_identification_type"))
    private IdentificationTypeJpaEntity identificationType;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "identification_number", nullable = false, length = 20)
    private String identificationNumber;

    @Column(
            name = "base_salary",
            nullable = false,
            precision = 15,
            scale = 2,
            columnDefinition = "DECIMAL(15,2) CHECK (base_salary >= 0 AND base_salary <= 15000000)"
    )
    private BigDecimal baseSalary;
}