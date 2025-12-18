package com.cotrafa.creditapproval.role.domain.model;


import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Permission {
    private UUID id;
    private UUID entityId;

    private boolean canCreate;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canDelete;
}
