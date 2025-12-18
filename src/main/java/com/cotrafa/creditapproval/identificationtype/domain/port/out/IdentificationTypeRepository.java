package com.cotrafa.creditapproval.identificationtype.domain.port.out;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentificationTypeRepository {
    IdentificationType save(IdentificationType identificationType);
    Optional<IdentificationType> findById(UUID id);
    boolean existsByName(String name);
    void deleteById(UUID id);
    PaginatedResult<IdentificationType> findAll(PaginationCriteria criteria);
    List<IdentificationType> findAllActive();
}