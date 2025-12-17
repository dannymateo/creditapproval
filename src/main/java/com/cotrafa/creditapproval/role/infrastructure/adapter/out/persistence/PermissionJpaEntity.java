package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence.SystemEntityJpaEntity;
import jakarta.persistence.*;
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
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private SystemEntityJpaEntity entity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

    @Column(name = "can_create", nullable = false)
    private boolean canCreate;

    @Column(name = "can_read", nullable = false)
    private boolean canRead;

    @Column(name = "can_update", nullable = false)
    private boolean canUpdate;

    @Column(name = "can_delete", nullable = false)
    private boolean canDelete;
}