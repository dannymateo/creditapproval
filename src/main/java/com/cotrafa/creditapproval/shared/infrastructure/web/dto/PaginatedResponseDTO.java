package com.cotrafa.creditapproval.shared.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
        description = "Respuesta paginada genérica que envuelve cualquier tipo de datos con metadatos de paginación. " +
                "Se utiliza en todos los endpoints que retornan listas grandes de información para " +
                "optimizar el rendimiento y mejorar la experiencia del usuario en la interfaz.",
        example = "{\"items\": [{...}], \"meta\": {\"currentPage\": 0, \"itemsPerPage\": 10, " +
                "\"totalItems\": 50, \"totalPages\": 5}}"
)
public class PaginatedResponseDTO<T> {

    @Schema(
            description = "Lista de elementos de la página actual. El tipo de datos depende del endpoint consultado " +
                    "(usuarios, roles, clientes, solicitudes, etc.)",
            example = "[{...}, {...}, {...}]"
    )
    private List<T> items;

    @Schema(
            description = "Metadatos de la paginación que incluyen información sobre la página actual, " +
                    "total de elementos y total de páginas disponibles"
    )
    private MetaDTO meta;

    @Data
    @Builder
    @Schema(
            description = "Metadatos de paginación que proporcionan información sobre el estado actual " +
                    "de la paginación y el total de datos disponibles en el servidor",
            example = "{\"currentPage\": 0, \"itemsPerPage\": 10, \"totalItems\": 50, \"totalPages\": 5}"
    )
    public static class MetaDTO {

        @Schema(
                description = "Número de la página actual (basado en 0). Primera página = 0, segunda = 1, etc.",
                example = "0",
                minimum = "0"
        )
        private int currentPage;

        @Schema(
                description = "Cantidad de elementos por página (tamaño de página). " +
                        "Define cuántos registros se muestran en cada página.",
                example = "10",
                minimum = "1",
                maximum = "100"
        )
        private int itemsPerPage;

        @Schema(
                description = "Número total de elementos disponibles en el servidor que cumplen con los criterios de búsqueda. " +
                        "Útil para calcular el progreso y mostrar información al usuario.",
                example = "50",
                minimum = "0"
        )
        private long totalItems;

        @Schema(
                description = "Número total de páginas disponibles. " +
                        "Se calcula dividiendo totalItems entre itemsPerPage (redondeando hacia arriba).",
                example = "5",
                minimum = "0"
        )
        private int totalPages;
    }

    @Schema(hidden = true)
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
