package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IdentificationTypeJpaRepository extends JpaRepository<IdentificationTypeJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT COUNT(c) > 0 FROM CustomerJpaEntity c WHERE c.identificationType.id = :identificationTypeId")
    boolean isIdentificationTypeAssignedToCustomers(@Param("identificationTypeId") UUID identificationTypeId);
    List<IdentificationTypeJpaEntity> findAllByActiveTrue();
    Page<IdentificationTypeJpaEntity> findByNameContainingIgnoreCase(
            String name, Pageable pageable
    );
}
