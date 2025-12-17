package com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence.RoleJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_session_id", columnNames = "session_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJpaEntity extends Auditable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String lastPassword;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private int loginAttempts = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean logged = false;

    @Column(name = "session_id")
    private UUID sessionId;

    @Builder.Default
    @Column(nullable = false, name = "last_password_change")
    private Instant lastPasswordChange = Instant.now();

    private Instant unlockDate;
    private Instant lastLogin;
}