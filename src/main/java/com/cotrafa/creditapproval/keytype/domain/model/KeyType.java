package com.cotrafa.creditapproval.keytype.domain.model;

import lombok.Data;
import java.util.UUID;

@Data
public class KeyType {
    private UUID id;
    private String name;
}