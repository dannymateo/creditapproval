package com.cotrafa.creditapproval.loanrequest.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AIRecommendation {
    private final String decision;    // Ejemplo: "APROBAR", "RECHAZAR"
    private final String riskLevel;   // Ejemplo: "BAJO", "MEDIO", "ALTO"
    private final String advice;      // El texto detallado de ChatGPT
}