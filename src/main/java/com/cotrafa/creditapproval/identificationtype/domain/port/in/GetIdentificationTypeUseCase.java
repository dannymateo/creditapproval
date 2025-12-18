package com.cotrafa.creditapproval.identificationtype.domain.port.in;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;

import java.util.List;
import java.util.UUID;

public interface GetIdentificationTypeUseCase {
    IdentificationType getById(UUID id);
    PaginatedResult<IdentificationType> getAll(PaginationCriteria criteria);
    List<IdentificationType> getAllActive();
}
