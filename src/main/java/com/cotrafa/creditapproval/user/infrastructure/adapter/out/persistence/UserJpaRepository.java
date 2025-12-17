package com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
    Page<UserJpaEntity> findByEmailContainingIgnoreCase(
            String name, String email, Pageable pageable
    );
}