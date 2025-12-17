package com.cotrafa.creditapproval.role.domain.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Permission {
    private UUID id;
    private UUID entityId;

    private boolean canCreate;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canDelete;
}
