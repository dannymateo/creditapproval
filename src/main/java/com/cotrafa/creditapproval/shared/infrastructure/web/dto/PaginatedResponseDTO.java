package com.cotrafa.creditapproval.shared.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO<T> {

    private List<T> items;
    private MetaDTO meta;

    @Data
    @Builder
    public static class MetaDTO {
        private int currentPage;
        private int itemsPerPage;
        private long totalItems;
        private int totalPages;
    }

    public static <T> PaginatedResponseDTO<T> from(Page<?> pageData, List<T> dtoList) {
        return PaginatedResponseDTO.<T>builder()
                .items(dtoList)
                .meta(MetaDTO.builder()
                        .currentPage(pageData.getNumber())
                        .itemsPerPage(pageData.getSize())
                        .totalItems(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .build())
                .build();
    }
}