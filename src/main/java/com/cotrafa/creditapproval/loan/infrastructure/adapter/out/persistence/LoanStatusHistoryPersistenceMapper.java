package com.cotrafa.creditapproval.loan.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.loan.domain.model.LoanStatusHistory;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface LoanStatusHistoryPersistenceMapper {

    @Mapping(target = "loan.id", source = "loanId")
    @Mapping(target = "loanStatus.id", source = "loanStatusId")
    LoanStatusHistoryJpaEntity toEntity(LoanStatusHistory domain);

    @Mapping(target = "loanId", source = "loan.id")
    @Mapping(target = "loanStatusId", source = "loanStatus.id")
    LoanStatusHistory toDomain(LoanStatusHistoryJpaEntity entity);
}
