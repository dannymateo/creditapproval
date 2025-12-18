package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanRequestPersistenceMapper {
    LoanRequestJpaEntity toEntity(LoanRequest domain);
    LoanRequest toDomain(LoanRequestJpaEntity entity);

    LoanRequestStatusHistoryJpaEntity toHistoryEntity(LoanRequestStatusHistory domain);
    LoanRequestStatusHistory toHistoryDomain(LoanRequestStatusHistoryJpaEntity entity);
}
