package com.cotrafa.creditapproval.shared.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationCriteria {
    private int page;
    private int size;
    private String search;
    private String sortBy;
    private String sortDirection;
}