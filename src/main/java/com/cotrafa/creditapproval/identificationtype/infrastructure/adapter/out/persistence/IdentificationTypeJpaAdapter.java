package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.domain.port.out.IdentificationTypeRepository;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence.LoanTypeJpaEntity;
import com.cotrafa.creditapproval.role.domain.model.Role;
import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.infrastructure.adapter.out.persistence.UserJpaEntity;
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
public class IdentificationTypeJpaAdapter implements IdentificationTypeRepository {
    private final IdentificationTypeJpaRepository jpaRepository;
    private final IdentificationTypePersistenceMapper mapper;

    @Override
    public IdentificationType save(IdentificationType idType) {
        IdentificationTypeJpaEntity entity = mapper.toEntity(idType);
        IdentificationTypeJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<IdentificationType> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public PaginatedResult<IdentificationType> findAll(PaginationCriteria criteria) {

        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("name")
        );

        Page<IdentificationTypeJpaEntity> entityPage;
        String searchTerm = criteria.getSearch();

        if (StringUtils.hasText(searchTerm)) {
            entityPage = jpaRepository.findByNameContainingIgnoreCase(
                    searchTerm, pageable
            );
        } else {
            entityPage = jpaRepository.findAll(pageable);
        }

        List<IdentificationType> domainIdentificationTypes = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PaginatedResult<>(
                domainIdentificationTypes,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages()
        );
    }

    @Override
    public List<IdentificationType> findAllActive() {
        return jpaRepository.findAllByActiveTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
