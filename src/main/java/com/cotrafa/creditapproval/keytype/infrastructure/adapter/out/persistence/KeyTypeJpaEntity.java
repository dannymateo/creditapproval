package com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "key_types", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyTypeJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "The key type name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}