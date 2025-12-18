package com.cotrafa.creditapproval.identificationtype.infrastructure.adapter.in.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class IdentificationTypeSelectResponse {
    private UUID id;
    private String name;
}
