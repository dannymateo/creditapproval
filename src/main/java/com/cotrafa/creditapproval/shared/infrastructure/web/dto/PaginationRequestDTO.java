package com.cotrafa.creditapproval.shared.infrastructure.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PaginationRequestDTO {

    @Min(value = 0, message = "Page number must be 0 or greater")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100 items")
    private int size = 10;

    @Size(max = 50, message = "Search query cannot exceed 50 characters")
    private String search;

    @Size(max = 50, message = "Sort field name cannot exceed 50 characters")
    private String sortBy = "createdAt";

    @Pattern(regexp = "^(?i)(ASC|DESC)$", message = "Sort direction must be 'ASC' or 'DESC'")
    private String sortDirection = "DESC";


    public Pageable toPageable() {
        // 1. Determine Direction
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 2. Determine Sort Field (Fallback to 'createdAt' if null or empty)
        String sortField = (sortBy == null || sortBy.trim().isEmpty()) ? "createdAt" : sortBy;

        // 3. Build PageRequest
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}