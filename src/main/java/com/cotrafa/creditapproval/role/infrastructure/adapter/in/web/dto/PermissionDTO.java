package com.cotrafa.creditapproval.role.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDTO {

    @NotNull(message = "System Entity ID is required")
    private UUID systemEntityId;

    @NotNull(message = "canCreate status is required")
    private Boolean canCreate;

    @NotNull(message = "canRead status is required")
    private Boolean canRead;

    @NotNull(message = "canUpdate status is required")
    private Boolean canUpdate;

    @NotNull(message = "canDelete status is required")
    private Boolean canDelete;
}