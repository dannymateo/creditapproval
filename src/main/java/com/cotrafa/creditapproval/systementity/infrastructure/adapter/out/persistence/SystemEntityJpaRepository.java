package com.cotrafa.creditapproval.systementity.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SystemEntityJpaRepository extends JpaRepository<SystemEntityJpaEntity, UUID> {
}