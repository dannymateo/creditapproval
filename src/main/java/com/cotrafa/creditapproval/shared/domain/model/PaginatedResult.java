package com.cotrafa.creditapproval.shared.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResult<T> {
    private final List<T> items;
    private final int currentPage;
    private final int itemsPerPage;
    private final long totalItems;
    private final int totalPages;
}