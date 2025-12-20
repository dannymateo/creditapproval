package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.ai;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.AiAdvisorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class OpenAiAdapter implements AiAdvisorPort {

    private final WebClient webClient;
    private final String apiKey;

    // Configuramos el formato de moneda colombiano: puntos para miles, coma para centavos
    private final DecimalFormat moneyFormat;

    public OpenAiAdapter(WebClient.Builder webClientBuilder, @Value("${ai.openai.api-key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
        this.apiKey = apiKey;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY); // Alemania usa . miles y , decimales
        this.moneyFormat = new DecimalFormat("#,##0.00", symbols);
    }

    @Override
    public String getLoanAdvice(LoanRequest request, Customer customer, List<LoanRequest> activeLoans, String currentStatus) {

        double deudaMensualActual = activeLoans.stream()
                .mapToDouble(this::calculateMonthlyQuote)
                .sum();

        // Enviamos los datos ya formateados desde Java para guiar a la IA
        String userContext = String.format(
                "CASO COTRAFA:\n" +
                        "- ASOCIADO: %s %s | Salario: $%s\n" +
                        "- DEUDA ACTUAL (CUOTAS): $%s\n" +
                        "- NUEVA SOLICITUD: $%s a %d meses | Tasa: %s%% E.A.\n",
                customer.getFirstName(), customer.getLastName(),
                moneyFormat.format(customer.getBaseSalary()),
                moneyFormat.format(deudaMensualActual),
                moneyFormat.format(request.getAmount()),
                request.getTermMonths(),
                request.getAnnualRate()
        );

        String systemInstructions =
                "ROL: Analista de Riesgos Senior Cotrafa. Tono: Directo y Matemático.\n" +
                        "REGLAS DE ORO (PROHIBIDO ALUCINAR):\n" +
                        "1. Si A > B, no digas que A es menor que B.\n" +
                        "2. Usa TasaMensual = (TasaAnual / 12 / 100).\n" +
                        "PROCEDIMIENTO OBLIGATORIO:\n" +
                        "- Paso 1: Calcula Capacidad Máxima (Salario * 0.35).\n" +
                        "- Paso 2: Calcula Capacidad Disponible (Máxima - Deuda Actual).\n" +
                        "- Paso 3: Calcula Cuota Nueva con fórmula de amortización.\n" +
                        "- Paso 4: COMPARA Cuota Nueva vs Capacidad Disponible.\n" +
                        "- Paso 5: COMPARA Monto vs (Salario * 5).\n" +
                        "DECISIÓN:\n" +
                        "- Si Cuota Nueva > Capacidad Disponible -> RECHAZADO (Sin excepción).\n" +
                        "- Si Monto > (Salario * 5) -> REVISIÓN MANUAL.\n" +
                        "- De lo contrario -> APROBADO.\n" +
                        "FORMATO: [Argumento con números formateados $#.###,00]. Decisión Final: [RESULTADO].";

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", systemInstructions),
                        Map.of("role", "user", "content", userContext)
                ),
                "temperature", 0.0
        );

        try {
            return webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(java.time.Duration.ofSeconds(15))
                    .map(res -> {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
                        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
                    })
                    .onErrorReturn("SISTEMA: Error de tiempo de respuesta.")
                    .block();
        } catch (Exception e) {
            return "SISTEMA: Error de conexión: " + e.getMessage();
        }
    }

    private double calculateMonthlyQuote(LoanRequest loan) {
        double p = loan.getAmount().doubleValue();
        double annualRate = loan.getAnnualRate().doubleValue() / 100;
        double i = annualRate / 12;
        int n = loan.getTermMonths();
        if (i == 0) return p / n;
        return p * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
    }
}