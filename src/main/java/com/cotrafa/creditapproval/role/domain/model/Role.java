package com.cotrafa.creditapproval.role.domain.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {
    private UUID id;
    private String name;
    private Boolean active;
    private List<Permission> permissions = new ArrayList<>();
}