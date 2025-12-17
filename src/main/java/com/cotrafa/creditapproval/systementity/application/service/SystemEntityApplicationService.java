package com.cotrafa.creditapproval.systementity.application.service;

import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import com.cotrafa.creditapproval.systementity.domain.port.in.GetSystemEntitiesUseCase;
import com.cotrafa.creditapproval.systementity.domain.port.out.SystemEntityRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemEntityApplicationService implements GetSystemEntitiesUseCase {

    private final SystemEntityRepositoryPort repositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<SystemEntity> getAll() {
        return repositoryPort.findAllOrdered();
    }

    @Override
    @Transactional(readOnly = true)
    public SystemEntity getById(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("System Entity not found"));
    }
}