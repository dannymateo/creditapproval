package com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import com.cotrafa.creditapproval.user.domain.model.User;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.CreateUserDTO;
import com.cotrafa.creditapproval.user.infrastructure.adapter.in.web.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toDomain(CreateUserDTO dto);

    UserResponse toResponse(User domain);
}
