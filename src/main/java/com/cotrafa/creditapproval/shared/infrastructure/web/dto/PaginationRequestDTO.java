package com.cotrafa.creditapproval.shared.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Schema(
        description = "Parámetros de paginación, búsqueda y ordenamiento para consultas de listados. " +
                "Se utiliza como query parameters en las peticiones GET que retornan listas grandes de datos. " +
                "Permite controlar qué página ver, cuántos elementos mostrar, buscar texto y ordenar resultados.",
        example = "{\"page\": 0, \"size\": 10, \"search\": \"Juan\", \"sortBy\": \"createdAt\", \"sortDirection\": \"DESC\"}"
)
public class PaginationRequestDTO {

    @Schema(
            description = "Número de página a consultar (basado en 0). " +
                    "Primera página = 0, segunda = 1, etc. " +
                    "Si no se especifica, se retorna la primera página.",
            example = "0",
            minimum = "0",
            defaultValue = "0"
    )
    @Min(value = 0, message = "Page number must be 0 or greater")
    private int page = 0;

    @Schema(
            description = "Cantidad de elementos por página (tamaño de página). " +
                    "Define cuántos registros se retornan en cada petición. " +
                    "Mínimo 1, máximo 100. Por defecto 10 elementos.",
            example = "10",
            minimum = "1",
            maximum = "100",
            defaultValue = "10"
    )
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100 items")
    private int size = 10;

    @Schema(
            description = "Término de búsqueda para filtrar resultados. " +
                    "Se aplica sobre campos relevantes del recurso consultado (nombre, email, etc.). " +
                    "Búsqueda case-insensitive. Máximo 50 caracteres. Campo opcional.",
            example = "Juan",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Size(max = 50, message = "Search query cannot exceed 50 characters")
    private String search;

    @Schema(
            description = "Campo por el cual ordenar los resultados. " +
                    "Debe ser un campo válido de la entidad consultada. " +
                    "Ejemplos comunes: 'createdAt', 'name', 'email', 'id'. " +
                    "Por defecto se ordena por 'createdAt' (fecha de creación).",
            example = "createdAt",
            maxLength = 50,
            defaultValue = "createdAt"
    )
    @Size(max = 50, message = "Sort field name cannot exceed 50 characters")
    private String sortBy = "createdAt";

    @Schema(
            description = "Dirección del ordenamiento. " +
                    "ASC = Ascendente (A-Z, 0-9, más antiguos primero). " +
                    "DESC = Descendente (Z-A, 9-0, más recientes primero). " +
                    "No es case-sensitive. Por defecto DESC (más recientes primero).",
            example = "DESC",
            allowableValues = {"ASC", "DESC", "asc", "desc"},
            defaultValue = "DESC"
    )
    @Pattern(regexp = "^(?i)(ASC|DESC)$", message = "Sort direction must be 'ASC' or 'DESC'")
    private String sortDirection = "DESC";

    @Schema(hidden = true)
    public Pageable toPageable() {
        // 1. Determine Direction
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 2. Determine Sort Field (Fallback to 'createdAt' if null or empty)
        String sortField = (sortBy == null || sortBy.trim().isEmpty()) ? "createdAt" : sortBy;

        // 3. Build PageRequest
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}
