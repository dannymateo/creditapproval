package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.identificationtype.domain.model.IdentificationType;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.CreateIdentificationTypeDTO;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.IdentificationTypeResponse;
import com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto.UpdateIdentificationTypeDTO;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface IdentificationTypeMapper {

    @Mapping(target = "id", ignore = true)
    IdentificationType toDomain(CreateIdentificationTypeDTO dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "active", target = "active")
    @Mapping(target = "id", ignore = true)
    IdentificationType toDomain(UpdateIdentificationTypeDTO dto);

    IdentificationTypeResponse toResponse(IdentificationType domain);
}