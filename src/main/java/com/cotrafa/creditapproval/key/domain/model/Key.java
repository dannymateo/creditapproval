package com.cotrafa.creditapproval.key.domain.model;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Key {
    private UUID id;
    private UUID keyTypeId;
    private UUID userId;
    private String key;
    private boolean active;
    private int attempts;
    private Instant expiredAt;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiredAt);
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}