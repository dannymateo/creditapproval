package com.cotrafa.creditapproval.key.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence.KeyTypeJpaEntity;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyJpaEntity extends Auditable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_id", nullable = false)
    private KeyTypeJpaEntity keyType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @Column(name = "key_value", nullable = false)
    private String key;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private Instant expiredAt;
}