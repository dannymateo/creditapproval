package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleJpaEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Role name cannot be empty")
    @Size(min = 3, max = 100, message = "Role name must be between 3 and 100 characters")
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @NotNull(message = "Active status is mandatory")
    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PermissionJpaEntity> permissions;
}