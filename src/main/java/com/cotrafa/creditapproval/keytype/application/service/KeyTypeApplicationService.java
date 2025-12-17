package com.cotrafa.creditapproval.keytype.application.service;

import com.cotrafa.creditapproval.keytype.domain.model.KeyType;
import com.cotrafa.creditapproval.keytype.domain.port.in.FindOrCreateKeyTypeUseCase;
import com.cotrafa.creditapproval.keytype.domain.port.out.KeyTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyTypeApplicationService implements FindOrCreateKeyTypeUseCase {

    private final KeyTypeRepositoryPort repositoryPort;

    @Override
    @Transactional
    public KeyType findOrCreate(String name) {
        return repositoryPort.findByName(name)
                .orElseGet(() -> {
                    KeyType newType = new KeyType();
                    newType.setName(name);
                    return repositoryPort.save(newType);
                });
    }
}