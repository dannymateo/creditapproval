package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class IdentificationTypeResponse {
    private UUID id;
    private String name;
    private boolean active;
}
