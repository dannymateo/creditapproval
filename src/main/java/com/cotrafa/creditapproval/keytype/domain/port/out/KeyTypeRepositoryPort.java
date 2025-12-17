package com.cotrafa.creditapproval.keytype.domain.port.out;

import com.cotrafa.creditapproval.keytype.domain.model.KeyType;
import java.util.Optional;

public interface KeyTypeRepositoryPort {
    Optional<KeyType> findByName(String name);
    KeyType save(KeyType keyType);
}