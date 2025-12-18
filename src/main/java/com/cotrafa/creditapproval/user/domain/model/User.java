package com.cotrafa.creditapproval.user.domain.model;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private UUID id;
    private String email;
    private String password;
    private String lastPassword;
    private Boolean active;
    private UUID roleId;

    private int loginAttempts;
    private Boolean logged;
    private UUID sessionId;
    private Instant lastPasswordChange;
    private Instant unlockDate;
    private Instant lastLogin;

    public Boolean isLocked() {
        return unlockDate != null && unlockDate.isAfter(Instant.now());
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.unlockDate = null;
    }
}