package com.cotrafa.creditapproval.key.domain.port.out;

import com.cotrafa.creditapproval.key.domain.model.Key;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepositoryPort {
    Key save(Key key);
    Optional<Key> findActiveKey(UUID userId, String keyTypeName);
}