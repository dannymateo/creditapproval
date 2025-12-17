package com.cotrafa.creditapproval.keytype.domain.port.in;

import com.cotrafa.creditapproval.keytype.domain.model.KeyType;

public interface FindOrCreateKeyTypeUseCase {
    KeyType findOrCreate(String name);
}