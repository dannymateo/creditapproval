package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.loantype.domain.model.LoanType;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.CreateLoanTypeDTO;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.LoanTypeSelectResponse;
import com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto.UpdateLoanTypeDTO;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface LoanTypeMapper {

    @Mapping(target = "id", ignore = true)
    LoanType toDomain(CreateLoanTypeDTO dto);

    @Mapping(target = "id", ignore = true)
    LoanType toDomain(UpdateLoanTypeDTO dto);

    LoanTypeResponse toResponse(LoanType domain);

    LoanTypeSelectResponse toSelectResponse(LoanType domain);
}
