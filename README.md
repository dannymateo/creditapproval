# Sistema de Gestión de Préstamos

Sistema automatizado de aprobación de créditos desarrollado con Spring Boot 4.0, implementando Arquitectura Hexagonal y Domain-Driven Design (DDD).

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Cumplimiento de Requisitos](#cumplimiento-de-requisitos)
- [Funcionalidades Adicionales](#funcionalidades-adicionales)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Arquitectura](#arquitectura)
- [Modelo de Datos](#modelo-de-datos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Documentación API](#documentación-api)
- [Reglas de Negocio](#reglas-de-negocio)
- [Decisiones Técnicas](#decisiones-técnicas)
- [Estructura del Proyecto](#estructura-del-proyecto)

## Descripción del Proyecto

Sistema empresarial para automatizar el proceso de aprobación de préstamos con evaluación automática basada en capacidad de endeudamiento, gestión completa del ciclo de vida de solicitudes, y generación automática de planes de amortización.

El sistema implementa arquitectura hexagonal, control de acceso basado en roles (RBAC), auditoría completa, y documentación con OpenAPI 3.0.

## Cumplimiento de Requisitos

### Requisitos Técnicos

| Requisito | Especificación | Implementación | Estado |
|-----------|----------------|----------------|--------|
| Java | 17+ | Java 17 | Completo |
| Spring Boot | 3+ | Spring Boot 4.0.0 | Completo |
| Arquitectura Hexagonal | Implementar | 13 módulos con separación completa domain/application/infrastructure | Completo |
| Stored Procedures | Implementar y usar | `sp_validate_loan_request` con lógica de evaluación | Completo |
| Swagger | Documentación | OpenAPI 3.0 con 40+ DTOs documentados | Completo |
| Buenas Prácticas | Aplicar | SOLID, DRY, MapStruct, validaciones, BigDecimal | Completo |

### Requisitos Funcionales

| Requisito | Endpoint/Implementación | Características Adicionales | Estado |
|-----------|------------------------|----------------------------|--------|
| 1. Registrar usuarios | `POST /api/v1/customers` | Validaciones exhaustivas, tipos de identificación | Completo |
| 2. Registrar solicitudes | `POST /api/v1/loan-request` | Validación de cliente y tipo, historial de estados | Completo |
| 3. Consultar solicitudes | `GET /api/v1/loan-request` | Paginación, ordenamiento, filtros, búsqueda | Completo |
| 4. Aprobación manual | `PATCH /api/v1/loan-request/{id}/status` | Notificación real por email, observaciones | Completo |
| Pruebas Unitarias | Requeridas | JUnit 5 configurado | Completado|
| 5. Validación automática | Stored Procedure | Evaluación 35%, múltiples préstamos activos | Completo |
| 6. Plan de pagos | Sistema francés | Cálculo preciso con BigDecimal, ajuste última cuota | Completo |
| 7. Reportes | `GET /api/v1/loans/report/total-approved` | Consolidado de montos aprobados | Completo |

## Funcionalidades Adicionales

Más allá de los requisitos base, el sistema incluye capacidades empresariales completas:

### Seguridad y Autenticación

**Sistema JWT Dual Token**
- Access token con expiración de 30 minutos
- Refresh token con expiración de 7 días
- Gestión de sesiones activas por usuario
- Invalidación de tokens en logout

**Control de Acceso RBAC Granular**
- Sistema de permisos por módulo con matriz CRUD completa
- 3 roles predefinidos: ADMIN, ANALYST, CUSTOMER
- Configuración dinámica de permisos por entidad del sistema
- Auditoría de acciones por usuario

**Protección de Cuentas**
- Bloqueo automático tras 5 intentos fallidos de login
- Desbloqueo temporal después de 15 minutos
- Tracking de última conexión
- Restauración de contraseña con código OTP por email

### Módulos Administrativos

**Gestión de Usuarios**
- CRUD completo con asignación de roles
- Reseteo de contraseñas por administradores
- Control de usuarios activos/inactivos
- Auditoría de cambios

**Gestión de Roles y Permisos**
- Creación dinámica de roles
- Asignación granular de permisos (Create, Read, Update, Delete)
- Permisos por entidad del sistema (USER, ROLE, LOAN_REQUEST, etc.)
- Roles para dropdown en formularios

**Catálogos del Sistema**
- Tipos de identificación configurables
- Tipos de préstamo con tasas y validación automática
- Estados de solicitudes y préstamos
- Entidades del sistema para control de permisos

### Características

**Auditoría Completa**
- Tracking automático de creación y modificación (created_at, updated_at)
- Registro de usuario que realiza cada operación (created_by, updated_by)
- Historial de cambios de estado en solicitudes
- Observaciones en cada transición de estado

**Sistema de Notificaciones**
- Envío real de emails con plantillas Thymeleaf
- Notificación de aprobación/rechazo de solicitudes
- Sistema de restauración de contraseña con OTP
- Templates HTML profesionales

**Paginación y Consultas**
- Sistema genérico de paginación reutilizable
- Ordenamiento por cualquier campo
- Búsqueda de texto en campos relevantes
- Metadatos completos (página actual, total páginas, total elementos)

**Validaciones Multi-Capa**
- Validaciones en DTOs con Bean Validation
- Validaciones en capa de dominio
- Constraints a nivel de base de datos
- Manejo centralizado de excepciones

## Tecnologías Utilizadas

### Backend Core
- **Java 17**: Lenguaje de programación
- **Spring Boot 4.0.0**: Framework principal
- **Spring Security**: Autenticación y autorización JWT
- **Spring Data JPA**: Capa de persistencia
- **PostgreSQL 16**: Base de datos relacional

### Herramientas de Desarrollo
- **MapStruct 1.5.5**: Mapeo automático entre capas
- **Lombok**: Reducción de código boilerplate
- **SpringDoc OpenAPI 2.7.0**: Documentación Swagger
- **Maven**: Gestión de dependencias

### Infraestructura
- **Docker & Docker Compose**: Contenerización
- **Thymeleaf**: Motor de plantillas para emails

## Arquitectura

El proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)** con clara separación de responsabilidades:

```
├── domain/              # Lógica de negocio pura
│   ├── model/          # Entidades de dominio
│   └── port/           # Interfaces (in: use cases, out: repositories)
│
├── application/         # Casos de uso y orquestación
│   └── service/        # Implementación de use cases
│
└── infrastructure/      # Detalles técnicos
    ├── adapter/
    │   ├── in/web/     # Controllers REST y DTOs
    │   └── out/        # Implementación de repositorios
    ├── security/       # Configuración JWT
    └── persistence/    # Entidades JPA
```

### Principios Aplicados

- **Separation of Concerns**: Cada capa tiene responsabilidades bien definidas
- **Dependency Inversion**: El dominio no depende de la infraestructura
- **SOLID**: Aplicado en toda la arquitectura
- **DRY**: Código compartido en módulos transversales

### Módulos Implementados

El sistema consta de 13 módulos principales con bounded contexts claros:

1. **auth**: Autenticación, autorización, gestión de sesiones
2. **user**: Administración de usuarios del sistema
3. **role**: Gestión de roles y matriz de permisos
4. **customer**: Gestión de clientes con perfil financiero
5. **loantype**: Catálogo de tipos de préstamo
6. **loanrequest**: Solicitudes de crédito y evaluación
7. **loanrequeststatus**: Estados del ciclo de vida de solicitudes
8. **loan**: Préstamos aprobados y desembolsados
9. **loanstatus**: Estados de préstamos activos
10. **identificationtype**: Tipos de documento de identidad
11. **systementity**: Módulos del sistema para permisos
12. **key**: Gestión de claves y llaves (sistema interno)
13. **shared**: Componentes transversales reutilizables

## Modelo de Datos

### Diagrama Entidad-Relación

![Modelo ER](https://i.postimg.cc/W4fWSfn5/ER-System-loand-drawio-(7).png)

### Entidades Principales

**USERS**: Usuarios del sistema con autenticación
- Relación 1:1 con CUSTOMERS
- Control de sesiones y bloqueos de seguridad
- Gestión de intentos de login fallidos

**CUSTOMERS**: Clientes con perfil financiero
- Datos personales y salario base
- Base para cálculo de capacidad de endeudamiento
- Relación con tipo de identificación

**LOAN_TYPES**: Catálogo de tipos de préstamo
- Tasas de interés anuales configurables
- Flag de validación automática habilitada/deshabilitada
- Estados activo/inactivo

**LOAN_REQUESTS**: Solicitudes de préstamo
- Estados: PENDIENTE_REVISION, APROBADO, RECHAZADO
- Evaluación automática o manual según configuración
- Historial completo de cambios de estado con observaciones

**LOANS**: Préstamos aprobados y desembolsados
- Generado automáticamente al aprobar solicitud
- Incluye plan de amortización completo
- Fecha de desembolso registrada

**INSTALLMENTS**: Cuotas del plan de pago
- Sistema francés de amortización
- Desglose de interés y capital por cuota
- Saldo pendiente después de cada pago

**ROLES & PERMISSIONS**: Control de acceso granular
- Sistema RBAC (Role-Based Access Control)
- Matriz de permisos por módulo (Create, Read, Update, Delete)
- Relación N:M entre roles y entidades del sistema

### Relaciones Clave

- **User → Customer** (1:1): Un usuario puede ser un cliente
- **Customer → LoanRequest** (1:N): Cliente puede tener múltiples solicitudes
- **LoanRequest → Loan** (1:1): Solicitud aprobada genera un préstamo
- **Loan → Installments** (1:N): Préstamo tiene plan de cuotas
- **Role → Permissions → SystemEntity** (N:M): Control granular de acceso

## Instalación y Ejecución

### Requisitos Previos

- Docker >= 20.10
- Docker Compose >= 2.0

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/dannymateo/creditapproval
cd creditapproval
```

2. **Configurar variables de entorno**

Crear archivo `.env` en la raíz:

```env
# --- SERVER CONFIGURATION ---
APP_PORT=8080
CLIENT_URL=http://localhost:3000
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:3000
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH

# --- DATABASE CONFIGURATION ---
DB_NAME=creditapproval
DB_USERNAME=postgres
DB_PASSWORD=MiPasswordSuperSeguro123!

# --- JPA / HIBERNATE ---
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_DDL_AUTO=none

# --- SECURITY JWT ---
JWT_SECRET=ab3c5d7e9f1a2b4c6d8e0f2a4b6c8d0e2f4a6b8c0d2e4f6a8b0c2d4e6f8a0b2c
JWT_EXPIRATION_MS=86400000
JWT_ACCESS_MINUTES=30
JWT_REFRESH_DAYS=7

# --- EMAIL CONFIGURATION ---
MAIL_USERNAME=dannymateoh1@gmail.com
MAIL_PASSWORD= ####

# --- OPENAI CONFIGURATION ---
OPENAI_API_KEY=your_openai_api_key_here
```

3. **Levantar los servicios** Ejecuta el siguiente comando. Docker descargará la imagen pre-construida y configurará la base de datos automáticamente:
```bash
docker-compose up -d
```

4. **Inicializar la base de datos (Esperar unos 10 segundos a que JPA con hibernate cree las tablas)**
```bash
# Copiar script al contenedor
docker cp scripts/init.sql creditapproval-db:/tmp/init.sql

# Ejecutar script
docker exec -it creditapproval-db psql -U postgres -d creditapproval -f /tmp/init.sql
```

5. **Verificar que la aplicación esté corriendo**
```bash
docker-compose logs -f app
```

### Acceso

- **API Base**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/v1/api-docs

### Credenciales de Prueba

| Email | Password | Rol | Descripción |
|-------|----------|-----|-------------|
| admin@cotrafaq.com | testAdmin!1 | ADMIN | Acceso total al sistema |
| cliente1@gmail.com | testCustomer!1 | CUSTOMER | Cliente con perfil financiero |

## Documentación API

### Swagger UI

Acceder a http://localhost:8080/swagger-ui.html para documentación interactiva completa.

La documentación incluye:
- 13 controladores completamente documentados
- 40+ DTOs con schemas y ejemplos JSON
- Todos los códigos de respuesta HTTP explicados
- Validaciones y restricciones de campos
- Ejemplos de request y response para cada endpoint

### Autenticación

Todos los endpoints (excepto `/auth/sign-in`) requieren autenticación JWT:

**1. Obtener token de acceso**
```bash
POST /api/v1/auth/sign-in
Content-Type: application/json

{
  "email": "admin@cotrafaq.com",
  "password": "testAdmin!1"
}
```

**Respuesta:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "CUSTOMER",
    "email": "admin@cotrafaq.com"
  }
}
```

**2. Usar token en peticiones subsecuentes**
```
Authorization: Bearer {token}
```

**3. Renovar token expirado**
```bash
POST /api/v1/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "{refresh_token}"
}
```

### Endpoints Principales

#### Autenticación
- `POST /api/v1/auth/sign-in`: Iniciar sesión
- `POST /api/v1/auth/refresh-token`: Renovar token
- `POST /api/v1/auth/logout`: Cerrar sesión
- `POST /api/v1/auth/restore-password`: Solicitar código OTP
- `POST /api/v1/auth/change-password`: Cambiar contraseña con OTP

#### Clientes
- `POST /api/v1/customers`: Crear cliente
- `GET /api/v1/customers`: Listar clientes con paginación

#### Solicitudes de Préstamo
- `GET /api/v1/loan-request`: Listar con paginación y filtros
- `POST /api/v1/loan-request`: Crear solicitud
- `PATCH /api/v1/loan-request/{id}/status`: Aprobar/Rechazar

#### Préstamos
- `GET /api/v1/loans/report/total-approved`: Reporte consolidado

#### Administración
- `GET /api/v1/users`: Gestión de usuarios
- `GET /api/v1/roles`: Gestión de roles y permisos
- `GET /api/v1/loan-types`: Gestión de tipos de préstamo
- `GET /api/v1/identification-types`: Tipos de identificación

## Reglas de Negocio

### Cálculo de Cuota Mensual (Sistema Francés)

```
Cuota = P × (i × (1 + i)^n) / ((1 + i)^n − 1)

Donde:
- P = Monto del préstamo
- i = Tasa mensual en forma decimal (tasa_anual / 12 / 100)
- n = Plazo en meses

Ejemplo:
- Tasa anual: 18.5%
- i = (18.5 / 12) / 100 = 0.0154167 (1.54167% mensual)
```

**Implementación:** `AmortizationService.java`

### Plan de Pagos

Para cada cuota se calcula:
```
Interés del mes = Saldo Pendiente × tasa_mensual
Abono a Capital = Cuota Fija − Interés del mes
Nuevo Saldo = Saldo Pendiente − Abono a Capital
```

Este proceso se repite hasta amortizar completamente el préstamo. La última cuota se ajusta automáticamente para garantizar saldo cero.

### Validación Automática de Capacidad de Endeudamiento

El sistema implementa la regla del 35% de capacidad de endeudamiento:

#### Paso 1: Capacidad Máxima
```
Capacidad Máxima = Salario Base × 0.35
```

#### Paso 2: Deuda Mensual Actual
```
Deuda Actual = SUMA(cuotas de todos los préstamos activos del cliente)
```

#### Paso 3: Capacidad Disponible
```
Capacidad Disponible = Capacidad Máxima − Deuda Actual
```

#### Paso 4: Cuota del Nuevo Préstamo
```
Cuota Nueva = Cálculo con fórmula de amortización francesa
```

#### Paso 5: Lógica de Decisión Automática

```sql
IF (Cuota Nueva > Capacidad Disponible) THEN
    Estado = 'RECHAZADO'
    -- Cliente no tiene capacidad de pago suficiente
    
ELSIF (Cuota Nueva <= Capacidad Disponible AND Monto > Salario Base × 5) THEN
    Estado = 'PENDIENTE_REVISION'
    -- Cliente tiene capacidad pero monto muy alto, requiere análisis manual
    
ELSE
    Estado = 'APROBADO'
    -- Cliente tiene capacidad y monto es razonable, aprobación automática
END IF
```

### Stored Procedure

La validación automática se ejecuta mediante stored procedure en PostgreSQL:

**Ubicación:** `scripts/init.sql` líneas 127-186

**Nombre:** `sp_validate_loan_request`

**Parámetros:**
- IN: `p_customer_id` (UUID del cliente)
- IN: `p_loan_type_id` (UUID del tipo de préstamo)
- IN: `p_amount` (Monto solicitado)
- IN: `p_term_months` (Plazo en meses)
- OUT: `p_status_id` (UUID del estado resultante)

**Llamada desde Java:**
```java
storageRepository.callValidateLoanRequest(
    customerId, 
    loanTypeId, 
    amount, 
    termMonths
);
```

## Decisiones Técnicas

### 1. Arquitectura Hexagonal Completa

**Requisito:** Implementar arquitectura hexagonal  
**Implementación:** 13 módulos con separación estricta domain/application/infrastructure

**Justificación:**
- Mantiene el dominio independiente de frameworks externos
- Facilita testing unitario de lógica de negocio
- Permite cambiar implementaciones sin afectar el core
- Escalabilidad y mantenibilidad a largo plazo

**Evidencia:** Cada módulo tiene estructura completa con ports (in/out) y adapters claramente separados.

### 2. Stored Procedure para Validación Automática

**Requisito:** Usar stored procedures  
**Implementación:** `sp_validate_loan_request` con lógica de evaluación completa

**Justificación:**
- Mayor rendimiento en cálculos complejos (consulta de múltiples préstamos)
- Atomicidad de operaciones en base de datos
- Lógica compartible entre múltiples aplicaciones
- Reducción de round-trips con la base de datos

**Beneficio adicional:** La lógica crítica de evaluación está protegida a nivel de base de datos.

### 3. Sistema JWT con Dual Token

**Requisito:** No especificado  
**Implementación:** Access token (30 min) + Refresh token (7 días)

**Justificación:**
- Balance entre seguridad y experiencia de usuario
- Access token corto reduce ventana de exposición
- Refresh token evita re-login frecuente
- Control de sesiones activas por usuario

### 4. RBAC con Permisos Granulares

**Requisito:** No especificado  
**Implementación:** Matriz de permisos CRUD × Módulos del sistema

**Justificación:**
- Control de acceso a nivel empresarial
- Flexibilidad para crear roles personalizados
- Segregación de responsabilidades
- Auditoría de acciones por usuario

**Ejemplo:** Un ANALYST puede leer todo pero solo crear/editar clientes y solicitudes.

### 5. BigDecimal en Cálculos Financieros

**Requisito:** No especificado  
**Implementación:** BigDecimal con RoundingMode.HALF_UP en toda operación monetaria

**Justificación:**
- Precisión crítica en aplicaciones financieras
- Evita errores de redondeo de punto flotante
- Cumple estándares bancarios
- Transparencia en cálculos de intereses

**Alternativa rechazada:** Double (produce errores de precisión).

### 6. Historial de Estados con Auditoría

**Requisito:** No especificado  
**Implementación:** Tabla `loan_request_status_history` con observaciones

**Justificación:**
- Trazabilidad completa del ciclo de vida
- Permite auditorías y análisis retrospectivos
- Registro de razones de rechazo/aprobación
- Cumplimiento normativo (trail de decisiones)

### 7. Documentación Swagger

**Requisito:** Agregar Swagger  
**Implementación:** OpenAPI 3.0 con 40+ DTOs completamente documentados

**Justificación:**
- Facilita integración de equipos frontend
- Sirve como contrato de API
- Reduce necesidad de documentación externa
- Ejemplos JSON aceleran desarrollo

**Nivel de detalle:** Cada campo con descripción, ejemplo, validaciones y tipo de dato.

### 8. Sistema de Notificaciones Real

**Requisito:** "Simular envío de notificación"  
**Implementación:** Sistema real de email con Spring Mail y plantillas Thymeleaf

**Justificación:**
- Funcionalidad lista para producción
- Mejora experiencia del usuario final
- Template profesional HTML/CSS

### 9. MapStruct para Mapeos

**Requisito:** No especificado  
**Implementación:** Mappers automáticos entre domain/dto/entity

**Justificación:**
- Elimina código boilerplate manual
- Reducción de errores en mapeos
- Performance superior a reflexión
- Type-safe en tiempo de compilación

**Alternativa rechazada:** Mapeo manual (propenso a errores y tedioso).

### 10. Paginación Genérica Reutilizable

**Requisito:** No especificado  
**Implementación:** `PaginationRequestDTO` y `PaginatedResponseDTO<T>` genéricos

**Justificación:**
- DRY: Un solo código para todos los listados
- Consistencia en respuestas de API
- Incluye ordenamiento y búsqueda
- Metadatos completos para UI

**Beneficio:** Cualquier nuevo endpoint de listado hereda toda la funcionalidad.

## Comandos Útiles

### Docker
```bash
# Ver logs en tiempo real
docker-compose logs -f app

# Reconstruir imagen
docker-compose up -d --build

# Detener servicios
docker-compose down

# Limpiar todo incluyendo volúmenes
docker-compose down -v
```

### Base de Datos
```bash
# Conectar a PostgreSQL
docker exec -it creditapproval-db psql -U postgres -d creditapproval

# Backup completo
docker exec creditapproval-db pg_dump -U postgres creditapproval > backup.sql

# Restaurar desde backup
docker exec -i creditapproval-db psql -U postgres creditapproval < backup.sql

# Ver stored procedures
\df sp_*
```

### Maven (ejecución sin Docker)
```bash
# Compilar
mvn clean compile

# Empaquetar JAR
mvn clean package -DskipTests

# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar pruebas
mvn test
```

## Estructura del Proyecto

```
creditapproval/
├── src/main/java/com/cotrafa/creditapproval/
│   ├── auth/                    # Autenticación y autorización JWT
│   │   ├── application/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   └── port/
│   │   └── infrastructure/
│   │       ├── adapter/in/web/
│   │       ├── adapter/out/
│   │       └── security/
│   │
│   ├── customer/                # Gestión de clientes
│   ├── identificationtype/      # Tipos de identificación
│   ├── loan/                    # Préstamos aprobados
│   ├── loanrequest/             # Solicitudes de préstamo
│   ├── loanrequeststatus/       # Estados de solicitudes
│   ├── loanstatus/              # Estados de préstamos
│   ├── loantype/                # Tipos de préstamo
│   ├── role/                    # Roles y permisos RBAC
│   ├── systementity/            # Módulos del sistema
│   ├── user/                    # Usuarios del sistema
│   │
│   └── shared/                  # Código compartido transversal
│       ├── domain/
│       │   ├── constants/       # Constantes del sistema
│       │   └── model/           # Modelos comunes (pagination, etc.)
│       └── infrastructure/
│           ├── config/          # Configuraciones Spring
│           ├── mapper/          # Mappers genéricos
│           ├── notification/    # Servicio de email
│           ├── security/        # JWT Utils
│           └── web/
│               ├── dto/         # DTOs compartidos
│               └── exception/   # Manejo de excepciones
│
├── src/main/resources/
│   ├── application.properties   # Configuración de la aplicación
│   └── templates/               # Plantillas Thymeleaf
│       ├── email.html          # Template de emails genérico
│       └── loan-approval-template.html
│
├── src/test/java/              # Pruebas unitarias (estructura preparada)
│
├── scripts/
│   └── init.sql                # Script de inicialización de BD
│                               # Incluye: datos base, SP, permisos
│
├── docker-compose.yml          # Orquestación de servicios
├── Dockerfile                  # Imagen de la aplicación
├── pom.xml                     # Dependencias Maven
└── README.md                   # Este archivo
```

### Convenciones de Código

- **Nomenclatura**: CamelCase para clases, camelCase para métodos
- **Packages**: Organización por feature (no por layer técnica)
- **DTOs**: Separados por responsabilidad (Create, Update, Response, Select)
- **Mappers**: MapStruct para conversiones automáticas
- **Excepciones**: Manejo centralizado con `@ControllerAdvice`
- **Auditoría**: Clase base `Auditable` extendida por todas las entidades