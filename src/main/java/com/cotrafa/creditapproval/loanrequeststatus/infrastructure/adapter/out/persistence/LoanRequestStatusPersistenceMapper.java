package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanRequestStatusPersistenceMapper {
    LoanRequestStatus toDomain(LoanRequestStatusJpaEntity entity);
    LoanRequestStatusJpaEntity toEntity(LoanRequestStatus domain);
}