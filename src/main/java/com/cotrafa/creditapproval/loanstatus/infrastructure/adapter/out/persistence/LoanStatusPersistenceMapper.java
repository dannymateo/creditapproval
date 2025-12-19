package com.cotrafa.creditapproval.loanstatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanStatusPersistenceMapper {

    LoanStatus toDomain(LoanStatusJpaEntity entity);

    @Mapping(target = "description", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    LoanStatusJpaEntity toEntity(LoanStatus domain);
}