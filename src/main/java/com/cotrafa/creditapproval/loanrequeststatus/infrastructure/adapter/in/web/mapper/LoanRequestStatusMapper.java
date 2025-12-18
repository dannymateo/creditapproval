package com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.loanrequeststatus.domain.model.LoanRequestStatus;
import com.cotrafa.creditapproval.loanrequeststatus.infrastructure.adapter.in.web.dto.LoanRequestStatusResponse;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface LoanRequestStatusMapper {
    LoanRequestStatusResponse toResponse(LoanRequestStatus domain);
}
