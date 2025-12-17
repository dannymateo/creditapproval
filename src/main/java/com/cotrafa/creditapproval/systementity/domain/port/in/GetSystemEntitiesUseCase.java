package com.cotrafa.creditapproval.systementity.domain.port.in;

import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;

import java.util.List;
import java.util.UUID;

public interface GetSystemEntitiesUseCase {
    List<SystemEntity> getAll();
    SystemEntity getById(UUID id);
}