package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.role.id = :roleId")
    boolean isRoleAssignedToUsers(@Param("roleId") UUID roleId);
    Page<RoleJpaEntity> findByNameContainingIgnoreCase(
            String name, Pageable pageable
    );
    @Query("SELECT r FROM RoleJpaEntity r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<RoleJpaEntity> findByIdWithPermissions(@Param("id") UUID id);
}