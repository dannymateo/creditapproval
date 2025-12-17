package com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.auth.domain.model.AuthSession;
import com.cotrafa.creditapproval.auth.domain.model.PasswordReset;
import com.cotrafa.creditapproval.auth.domain.model.UserCredentials;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto.ChangePasswordDTO;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto.LoginDTO;
import com.cotrafa.creditapproval.auth.infrastructure.adapter.in.web.dto.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserCredentials toDomain(LoginDTO request);

    PasswordReset toDomain(ChangePasswordDTO request);

    @Mapping(target = "token", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    LoginResponse toResponse(AuthSession session);
}