package com.cotrafa.creditapproval.identificationtype.domain.port.in;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;

public interface CreateIdentificationTypeUseCase {
    IdentificationType create(IdentificationType identificationType);
}
