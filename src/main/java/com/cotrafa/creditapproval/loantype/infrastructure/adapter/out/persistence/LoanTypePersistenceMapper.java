package com.cotrafa.creditapproval.loantype.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface LoanTypePersistenceMapper {
    LoanType toDomain(LoanTypeJpaEntity entity);
    LoanTypeJpaEntity toEntity(LoanType domain);
}
