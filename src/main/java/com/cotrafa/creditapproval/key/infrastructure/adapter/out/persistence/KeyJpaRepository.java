package com.cotrafa.creditapproval.key.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface KeyJpaRepository extends JpaRepository<KeyJpaEntity, UUID> {

    @Query("SELECT k FROM KeyJpaEntity k WHERE k.user.id = :userId AND k.keyType.name = :typeName AND k.active = true ORDER BY k.createdAt DESC LIMIT 1")
    Optional<KeyJpaEntity> findActiveKeyByUserAndType(@Param("userId") UUID userId, @Param("typeName") String typeName);
}