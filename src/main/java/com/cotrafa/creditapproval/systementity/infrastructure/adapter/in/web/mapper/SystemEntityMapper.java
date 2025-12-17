package com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import com.cotrafa.creditapproval.systementity.domain.model.SystemEntity; // <<-- DEBE SER ESTE IMPORT
import com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.dto.SystemEntityResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface SystemEntityMapper {

    SystemEntityResponse toResponse(SystemEntity domain);
}