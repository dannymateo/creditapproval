package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.CreateLoanRequestDTO;
import com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.in.web.dto.LoanRequestResponse;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface LoanRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "annualRate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LoanRequest toDomain(CreateLoanRequestDTO dto);

    @Mapping(target = "applicantName", source = "customerName")
    @Mapping(target = "applicantEmail", source = "customerEmail")
    @Mapping(target = "applicantSalary", source = "customerSalary")
    @Mapping(target = "loanTypeName", source = "loanTypeName")
    @Mapping(target = "status", source = "currentStatus")
    LoanRequestResponse toResponse(LoanRequest domain);
}