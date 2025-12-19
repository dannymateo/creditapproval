package com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.loan.domain.model.LoanReport;
import com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.dto.LoanReportResponse;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(config = CentralMapperConfig.class, imports = {LocalDateTime.class})
public interface LoanReportWebMapper {

    @Mapping(target = "currency", constant = "COP")
    @Mapping(target = "generatedAt", expression = "java(LocalDateTime.now())")
    LoanReportResponse toResponse(LoanReport domain);
}