package com.cotrafa.creditapproval.loantype.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        description = "Datos para actualizar un tipo de crédito existente. " +
                "Hereda los mismos campos que CreateLoanTypeDTO (name, annualRate, automaticValidation, active).",
        example = "{\"name\": \"Crédito Personal\", \"annualRate\": 19.0, \"automaticValidation\": false, \"active\": true}"
)
public class UpdateLoanTypeDTO extends CreateLoanTypeDTO {
}
