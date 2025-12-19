package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequestStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LoanRequestPersistenceMapper {

    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "loanType.id", source = "loanTypeId")
    @Mapping(target = "statusHistory", ignore = true)
    LoanRequestJpaEntity toEntity(LoanRequest domain);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "loanTypeId", source = "loanType.id")
    @Mapping(target = "customerName", expression = "java(entity.getCustomer().getFirstName() + \" \" + entity.getCustomer().getLastName())")
    @Mapping(target = "customerEmail", source = "customer.user.email")
    @Mapping(target = "customerSalary", source = "customer.baseSalary")
    @Mapping(target = "loanTypeName", source = "loanType.name")
    @Mapping(target = "currentStatus", source = "currentStatusName")

    LoanRequest toDomain(LoanRequestJpaEntity entity);
    @Mapping(target = "loanRequest.id", source = "loanRequestId")
    @Mapping(target = "loanRequestStatus.id", source = "loanRequestStatusId")
    LoanRequestStatusHistoryJpaEntity toHistoryEntity(LoanRequestStatusHistory domain);

    @Mapping(target = "loanRequestId", source = "loanRequest.id")
    @Mapping(target = "loanRequestStatusId", source = "loanRequestStatus.id")
    LoanRequestStatusHistory toHistoryDomain(LoanRequestStatusHistoryJpaEntity entity);
}