package com.cotrafa.creditapproval.role.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.role.domain.port.out.RoleRepositoryPort;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository jpaRepository;
    private final RolePersistenceMapper mapper;

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = mapper.toEntity(role);

        if (entity.getPermissions() != null) {
            entity.getPermissions().forEach(permission -> permission.setRole(entity));
        }

        RoleJpaEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findByIdWithPermissions(id).map(mapper::toDomain);
    }

    public Optional<Role> findByName(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean isRoleAssignedToUsers(UUID id) {
        return jpaRepository.isRoleAssignedToUsers(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public PaginatedResult<Role> findAll(PaginationCriteria criteria) {

        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("name")
        );

        Page<RoleJpaEntity> entityPage;
        String searchTerm = criteria.getSearch();

        if (StringUtils.hasText(searchTerm)) {
            entityPage = jpaRepository.findByNameContainingIgnoreCase(
                    searchTerm, pageable
            );
        } else {
            entityPage = jpaRepository.findAll(pageable);
        }

        List<Role> domainRole = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PaginatedResult<>(
                domainRole,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages()
        );
    }
}