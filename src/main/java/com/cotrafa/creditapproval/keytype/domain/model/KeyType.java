package com.cotrafa.creditapproval.keytype.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class KeyType {
    private UUID id;
    private String name;
}