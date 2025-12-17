package com.cotrafa.creditapproval.keytype.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface KeyTypeJpaRepository extends JpaRepository<KeyTypeJpaEntity, UUID> {
    Optional<KeyTypeJpaEntity> findByName(String name);
}