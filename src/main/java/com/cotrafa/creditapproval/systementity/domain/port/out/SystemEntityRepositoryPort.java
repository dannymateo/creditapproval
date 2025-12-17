package com.cotrafa.creditapproval.systementity.domain.port.out;

import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemEntityRepositoryPort {
    List<SystemEntity> findAllOrdered();
    Optional<SystemEntity> findById(UUID id);
}