package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "identification_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentificationTypeJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Identification type name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @NotNull(message = "Active status is mandatory")
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}