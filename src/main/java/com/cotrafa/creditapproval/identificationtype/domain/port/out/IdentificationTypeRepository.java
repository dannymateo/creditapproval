package com.cotrafa.creditapproval.identificationtype.domain.port.out;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentificationTypeRepository {
    IdentificationType save(IdentificationType identificationType);
    Optional<IdentificationType> findById(UUID id);
    boolean existsByName(String name);
    void deleteById(UUID id);
    List<IdentificationType> findAll();
}