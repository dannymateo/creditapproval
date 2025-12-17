package com.cotrafa.creditapproval.identificationtype.domain.port.in;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;

import java.util.UUID;

public interface UpdateIdentificationTypeUseCase {
    IdentificationType update(UUID id, IdentificationType identificationType);
}
