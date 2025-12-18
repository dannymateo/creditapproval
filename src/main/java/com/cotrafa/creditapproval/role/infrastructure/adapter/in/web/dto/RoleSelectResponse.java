package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSelectResponse {
    private UUID id;
    private String name;
}
