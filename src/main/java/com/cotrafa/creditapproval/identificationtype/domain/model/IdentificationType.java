package com.cotrafa.creditapproval.identificationtype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationType {
    private UUID id;
    private String name;
    private boolean active;
}