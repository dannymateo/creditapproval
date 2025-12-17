package com.cotrafa.creditapproval.role.domain.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Role {
    private UUID id;
    private String name;
    private Boolean active;
    private List<Permission> permissions = new ArrayList<>();
}