package com.cotrafa.creditapproval.shared.infrastructure.mapper;

import com.cotrafa.creditapproval.shared.domain.model.PaginatedResult;
import com.cotrafa.creditapproval.shared.domain.model.PaginationCriteria;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginatedResponseDTO;
import com.cotrafa.creditapproval.shared.infrastructure.web.dto.PaginationRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationWebMapper {

    public PaginationCriteria toDomain(PaginationRequestDTO requestDto) {
        return PaginationCriteria.builder()
                .page(requestDto.getPage())
                .size(requestDto.getSize())
                .search(requestDto.getSearch())
                .sortBy(requestDto.getSortBy())
                .sortDirection(requestDto.getSortDirection())
                .build();
    }

    public <D, R> PaginatedResponseDTO<R> toResponse(PaginatedResult<D> domainResult, List<R> mappedItems) {
        return PaginatedResponseDTO.<R>builder()
                .items(mappedItems)
                .meta(PaginatedResponseDTO.MetaDTO.builder()
                        .currentPage(domainResult.getCurrentPage())
                        .itemsPerPage(domainResult.getItemsPerPage())
                        .totalItems(domainResult.getTotalItems())
                        .totalPages(domainResult.getTotalPages())
                        .build())
                .build();
    }
}