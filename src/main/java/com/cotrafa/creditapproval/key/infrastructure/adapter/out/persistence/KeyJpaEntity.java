package com.cotrafa.creditapproval.key.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence.KeyTypeJpaEntity;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Key type is mandatory")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_id", nullable = false)
    private KeyTypeJpaEntity keyType;

    @NotNull(message = "User association is mandatory")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @NotBlank(message = "Key value cannot be empty")
    @Size(max = 255)
    @Column(name = "key_value", nullable = false, length = 255)
    private String key;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Min(value = 0, message = "Attempts cannot be negative")
    @Max(value = 10, message = "Maximum attempts exceeded")
    @Builder.Default
    @Column(nullable = false)
    private int attempts = 0;

    @NotNull(message = "Expiration date is mandatory")
    @Future(message = "Expiration date must be in the future")
    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;
}