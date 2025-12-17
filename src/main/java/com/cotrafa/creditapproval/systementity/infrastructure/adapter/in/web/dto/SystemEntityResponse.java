package com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class SystemEntityResponse {
    private UUID id;
    private String name;
    private String nameToViewClient;
    private String codGroup;
    private String nameGroup;
    private String path;
}