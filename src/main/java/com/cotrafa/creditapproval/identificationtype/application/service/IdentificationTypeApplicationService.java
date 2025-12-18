package com.cotrafa.creditapproval.identificationtype.application.service;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.CreateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.DeleteIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.GetIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.in.UpdateIdentificationTypeUseCase;
import com.cotrafa.creditapproval.identificationtype.domain.port.out.IdentificationTypeRepository;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.DatabaseConflictException;
import com.cotrafa.creditapproval.shared.infrastructure.web.exeption.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentificationTypeApplicationService implements
        CreateIdentificationTypeUseCase,
        UpdateIdentificationTypeUseCase,
        DeleteIdentificationTypeUseCase,
        GetIdentificationTypeUseCase {

    private final IdentificationTypeRepository repository;

    @Override
    @Transactional
    public IdentificationType create(IdentificationType identificationType) {
        if (repository.existsByName(identificationType.getName())) {
            throw new DatabaseConflictException("Identification type name already exists");
        }
        identificationType.setActive(true);
        return repository.save(identificationType);
    }

    @Override
    @Transactional
    public IdentificationType update(UUID id, IdentificationType domainRequest) {
        IdentificationType existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identification type not found"));

        if (domainRequest.getName() != null && !domainRequest.getName().isBlank() &&
                !existing.getName().equalsIgnoreCase(domainRequest.getName())) {

            if (repository.existsByName(domainRequest.getName())) {
                throw new DatabaseConflictException("Identification type name already exists");
            }
            existing.setName(domainRequest.getName());
        }

        existing.setActive(domainRequest.isActive());

        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!repository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Identification type not found");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public IdentificationType getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identification type not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdentificationType> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdentificationType> getAllActive() {
        return repository.findAllActive();
    }
}