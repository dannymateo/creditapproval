package com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {
    boolean existsByIdentificationTypeIdAndIdentificationNumber(UUID typeId, String number);
    Optional<CustomerJpaEntity> findByUserId(UUID userId);
}
