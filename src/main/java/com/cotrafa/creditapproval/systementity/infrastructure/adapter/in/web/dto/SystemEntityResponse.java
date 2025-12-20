package com.cotrafa.creditapproval.systementity.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;

@Data
@Schema(
        description = "Representa una entidad o módulo del sistema sobre el cual se pueden definir permisos. " +
                "Cada entidad corresponde a un recurso del sistema (User, Role, Customer, LoanRequest, etc.) " +
                "y se utiliza en la matriz de permisos de los roles.",
        example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"USER\", " +
                "\"nameToViewClient\": \"Usuarios\", \"codGroup\": \"ADMIN\", " +
                "\"nameGroup\": \"Administración\", \"path\": \"/users\"}"
)
public class SystemEntityResponse {

    @Schema(
            description = "Identificador único de la entidad del sistema",
            example = "123e4567-e89b-12d3-a456-426614174000",
            format = "uuid"
    )
    private UUID id;

    @Schema(
            description = "Nombre técnico de la entidad en mayúsculas. " +
                    "Se utiliza internamente para identificar el recurso. " +
                    "Ejemplos: USER, ROLE, CUSTOMER, LOAN_REQUEST, LOAN_TYPE.",
            example = "USER"
    )
    private String name;

    @Schema(
            description = "Nombre amigable de la entidad para mostrar en la interfaz de usuario. " +
                    "Descripción en español para que los administradores comprendan qué entidad están configurando.",
            example = "Usuarios"
    )
    private String nameToViewClient;

    @Schema(
            description = "Código del grupo al que pertenece la entidad. " +
                    "Agrupa entidades relacionadas para organización en la UI. " +
                    "Ejemplos: ADMIN, CATALOG, LOAN, CUSTOMER.",
            example = "ADMIN"
    )
    private String codGroup;

    @Schema(
            description = "Nombre del grupo al que pertenece la entidad. " +
                    "Descripción amigable del grupo para la interfaz.",
            example = "Administración"
    )
    private String nameGroup;

    @Schema(
            description = "Ruta o path asociado a la entidad en el sistema. " +
                    "Puede usarse para navegación o referencias.",
            example = "/users"
    )
    private String path;
}
