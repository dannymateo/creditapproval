# Integración con OpenAI

## Configuración

La integración con OpenAI está implementada siguiendo el patrón de Arquitectura Hexagonal y puede ser activada/desactivada mediante configuración.

### Variables de Entorno

Crea un archivo `.env` o configura las siguientes variables:

```bash
# Habilitar integración con OpenAI
OPENAI_ENABLED=true

# API Key de OpenAI (obligatorio si enabled=true)
OPENAI_API_KEY=sk-your-api-key-here

# Modelo a utilizar (opcional, default: gpt-3.5-turbo-1106)
OPENAI_MODEL=gpt-3.5-turbo-1106

# Timeout en segundos (opcional, default: 60)
OPENAI_TIMEOUT=60

# Máximo de tokens en respuesta (opcional, default: 1500)
OPENAI_MAX_TOKENS=1500

# Temperatura 0.0-2.0 (opcional, default: 0.7)
OPENAI_TEMPERATURE=0.7
```

### Obtener API Key

1. Visita [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)
2. Crea una cuenta o inicia sesión
3. Genera una nueva API key
4. Configúrala en la variable `OPENAI_API_KEY`

## Uso

### Con OpenAI Activado

Si `OPENAI_ENABLED=true`, el sistema usará OpenAI GPT para análisis:

```bash
GET /api/v1/loan-request/{id}/ai-recommendation
```

La IA analizará:
- Capacidad de endeudamiento
- Historial financiero
- Métricas de riesgo
- Reglas de negocio

Y retornará una recomendación fundamentada.

### Con Mock (Desarrollo)

Si `OPENAI_ENABLED=false` o no está configurado, el sistema usará el `MockAIAnalysisAdapter` que implementa lógica basada en reglas de negocio.

## Modelos Recomendados

### Para Producción
- **gpt-4-turbo-preview**: Mejor análisis, más costoso
- **gpt-4**: Balance entre calidad y costo

### Para Desarrollo
- **gpt-3.5-turbo-1106**: Rápido y económico
- **gpt-3.5-turbo**: Versión anterior estable

## Costos Estimados

Para análisis de solicitudes de crédito (aprox. 1000 tokens por análisis):

- **GPT-3.5-turbo**: ~$0.001 por análisis
- **GPT-4**: ~$0.03 por análisis
- **GPT-4-turbo**: ~$0.01 por análisis

## Arquitectura

La implementación sigue Arquitectura Hexagonal:

```
Domain Layer:
├── AIAnalysisService (Port Out)
└── GetAIRecommendationUseCase (Port In)

Infrastructure Layer:
├── OpenAIServiceImpl (Servicio genérico)
├── OpenAIAnalysisAdapter (Adaptador específico para préstamos)
└── MockAIAnalysisAdapter (Implementación mock)
```

El sistema selecciona automáticamente el adaptador según la configuración:
- Si `openai.enabled=true` → Usa `OpenAIAnalysisAdapter`
- Si `openai.enabled=false` → Usa `MockAIAnalysisAdapter`

## Estructura de Prompt

El prompt enviado a OpenAI incluye:

1. **System Prompt**: Define el rol de analista financiero
2. **User Content**: Datos estructurados de la solicitud:
   - Datos del solicitante
   - Detalles de la solicitud
   - Análisis de capacidad
   - Métricas financieras
   - Estado actual
3. **Response Schema**: Formato JSON esperado

## Respuesta de IA

```json
{
  "decision": "APPROVE|REJECT|REVIEW",
  "reasoning": "Justificación detallada...",
  "confidenceLevel": 0.85,
  "riskFactors": "Factores de riesgo identificados",
  "positiveFactors": "Factores positivos identificados"
}
```

## Testing

### Probar con Mock
```bash
# No configurar OPENAI_ENABLED o ponerlo en false
curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/v1/loan-request/{id}/ai-recommendation
```

### Probar con OpenAI
```bash
# Configurar OPENAI_ENABLED=true y OPENAI_API_KEY
export OPENAI_ENABLED=true
export OPENAI_API_KEY=sk-your-key

# Reiniciar aplicación y probar
curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/v1/loan-request/{id}/ai-recommendation
```

## Troubleshooting

### Error: "API key not configured"
- Verifica que `OPENAI_API_KEY` esté configurada
- Verifica que no tenga espacios al inicio/final

### Error: "Rate limit exceeded"
- Has excedido el límite de peticiones de OpenAI
- Espera un momento y reintenta
- Considera actualizar tu plan en OpenAI

### Error: "Model not found"
- El modelo configurado no existe o no tienes acceso
- Verifica `OPENAI_MODEL` en la configuración
- Usa un modelo disponible en tu cuenta

## Seguridad

- **NUNCA** comitees el API key en el código
- Usa variables de entorno o servicios de secrets management
- En producción, rota el API key periódicamente
- Monitorea el uso para detectar anomalías

