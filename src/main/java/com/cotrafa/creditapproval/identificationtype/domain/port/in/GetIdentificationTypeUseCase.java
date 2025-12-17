package com.cotrafa.creditapproval.identificationtype.domain.port.in;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;

import java.util.List;
import java.util.UUID;

public interface GetIdentificationTypeUseCase {
    IdentificationType getById(UUID id);
    List<IdentificationType> getAll();
}
