package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IdentificationTypeJpaRepository extends JpaRepository<IdentificationTypeJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    List<IdentificationTypeJpaEntity> findAllByActiveTrue();
    Page<IdentificationTypeJpaEntity> findByNameContainingIgnoreCase(
            String name, Pageable pageable
    );
}
