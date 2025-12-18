package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanRequestPersistenceMapper {

    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "loanType.id", source = "loanTypeId")
    LoanRequestJpaEntity toEntity(LoanRequest domain);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "loanTypeId", source = "loanType.id")
    LoanRequest toDomain(LoanRequestJpaEntity entity);

    @Mapping(target = "loanRequest.id", source = "loanRequestId")
    @Mapping(target = "loanRequestStatus.id", source = "loanRequestStatusId")
    LoanRequestStatusHistoryJpaEntity toHistoryEntity(LoanRequestStatusHistory domain);

    @Mapping(target = "loanRequestId", source = "loanRequest.id")
    @Mapping(target = "loanRequestStatusId", source = "loanRequestStatus.id")
    LoanRequestStatusHistory toHistoryDomain(LoanRequestStatusHistoryJpaEntity entity);
}