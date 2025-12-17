package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdentificationTypeJpaRepository extends JpaRepository<IdentificationTypeJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
