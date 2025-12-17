package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleResponse {
    private UUID id;
    private String name;
    private boolean active;
    private List<PermissionDTO> permissions;
}