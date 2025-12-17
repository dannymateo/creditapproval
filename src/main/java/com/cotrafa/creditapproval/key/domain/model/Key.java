package com.cotrafa.creditapproval.key.domain.model;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
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