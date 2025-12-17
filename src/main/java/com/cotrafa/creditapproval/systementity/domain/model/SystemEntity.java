package com.cotrafa.creditapproval.systementity.domain.model;

import lombok.Data;
import java.util.UUID;

@Data
public class SystemEntity {
    private UUID id;
    private String name;
    private String codGroup;
    private String nameGroup;
    private String path;
    private String nameToViewClient;
    private Integer order;
}