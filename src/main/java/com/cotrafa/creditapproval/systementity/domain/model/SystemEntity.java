package com.cotrafa.creditapproval.systementity.domain.model;


import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SystemEntity {
    private UUID id;
    private String name;
    private String codGroup;
    private String nameGroup;
    private String path;
    private String nameToViewClient;
    private Integer order;
}