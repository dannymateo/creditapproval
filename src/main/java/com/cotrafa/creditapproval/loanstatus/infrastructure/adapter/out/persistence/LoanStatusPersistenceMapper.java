package com.cotrafa.creditapproval.loanstatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanStatusPersistenceMapper {
    LoanStatus toDomain(LoanStatusJpaEntity entity);
    LoanStatusJpaEntity toEntity(LoanStatus domain);
}