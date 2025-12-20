package com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence.RoleJpaEntity;
import com.cotrafa.creditapproval.shared.infrastructure.persistence.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Role is mandatory")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255)
    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 255)
    private String lastPassword;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Min(value = 0, message = "Login attempts cannot be negative")
    @Max(value = 5, message = "Login attempts cannot exceed security limits")
    @Column(nullable = false)
    private int loginAttempts = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean logged = false;

    @Column(name = "session_id")
    private UUID sessionId;

    @NotNull
    @PastOrPresent(message = "Password change date cannot be in the future")
    @Builder.Default
    @Column(nullable = false, name = "last_password_change")
    private Instant lastPasswordChange = Instant.now();

    @Column(name = "unlock_date")
    private Instant unlockDate;

    @Column(name = "last_login")
    private Instant lastLogin;
}