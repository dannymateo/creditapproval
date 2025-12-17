package com.cotrafa.creditapproval.identificationtype.domain.port.in;

import java.util.UUID;

public interface DeleteIdentificationTypeUseCase {
    void delete(UUID id);
}
