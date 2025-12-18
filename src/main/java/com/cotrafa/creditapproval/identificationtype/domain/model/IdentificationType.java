package com.cotrafa.creditapproval.identificationtype.domain.model;


import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class IdentificationType {
    private UUID id;
    private String name;
    private boolean active;
}