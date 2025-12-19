package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.LoanInstallment;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface LoanPersistenceMapper {

    LoanJpaEntity toEntity(Loan domain);

    Loan toDomain(LoanJpaEntity entity);

    LoanInstallmentJpaEntity toEntity(LoanInstallment domain);

    LoanInstallment toDomain(LoanInstallmentJpaEntity entity);
}