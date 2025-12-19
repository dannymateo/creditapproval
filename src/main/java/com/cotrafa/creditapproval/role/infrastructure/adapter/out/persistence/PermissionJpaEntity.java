package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence.SystemEntityJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "System entity is mandatory for permission")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private SystemEntityJpaEntity entity;

    @NotNull(message = "Role is mandatory for permission")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

    @Builder.Default
    @Column(name = "can_create", nullable = false)
    private boolean canCreate = false;

    @Builder.Default
    @Column(name = "can_read", nullable = false)
    private boolean canRead = false;

    @Builder.Default
    @Column(name = "can_update", nullable = false)
    private boolean canUpdate = false;

    @Builder.Default
    @Column(name = "can_delete", nullable = false)
    private boolean canDelete = false;
}