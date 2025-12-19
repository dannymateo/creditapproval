-- ============================================================
-- DATABASE INITIALIZATION SCRIPT
-- Credit Approval System - COTRAFA
-- ============================================================
-- NOTE: Run this script MANUALLY after containers are up
-- OR wait for tables to be created by Hibernate first
-- ============================================================

-- ============================================================
-- BASE ROLES
-- ============================================================
INSERT INTO roles (id, created_at, updated_at, created_by, updated_by, name, active)
VALUES
    ('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', NOW(), NOW(), 'system', 'system', 'ADMIN', TRUE),
    ('b2c3d4e5-f6a7-4b6c-9d0e-1f2a3b4c5d6e', NOW(), NOW(), 'system', 'system', 'ANALYST', TRUE),
    ('c3d4e5f6-a7b8-4c7d-0e1f-2a3b4c5d6e7f', NOW(), NOW(), 'system', 'system', 'CUSTOMER', TRUE)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- USUARIOS
-- ============================================================
INSERT INTO users (
    id, created_at, updated_at, created_by, updated_by,
    role_id, email, password, last_password,
    active, login_attempts, logged, last_password_change
)
VALUES
    -- Admin Principal (ID: ...001) password: testAdmin!1
    ('00000000-0000-0000-0000-000000000001', NOW(), NOW(), 'system', 'system',
     'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', 'admin@cotrafaq.com',
     '$2a$10$B8N6HT.hTzAxcKHe8oahZOxnxGBkrP1eiHs0hKrcFFQLpeU.OXfQ6', '',
     TRUE, 0, FALSE, NOW()),

    -- Usuario para Cliente (ID: ...002) password: testCustomer!1
    ('00000000-0000-0000-0000-000000000002', NOW(), NOW(), 'system', 'system',
     'c3d4e5f6-a7b8-4c7d-0e1f-2a3b4c5d6e7f', 'cliente1@gmail.com',
     '$2a$10$jY2IbyXexCIuXMnRnr.uoO7y.NYBRGml3UZVbYZ4XJLxGZ55ANtg2', '',
     TRUE, 0, FALSE, NOW());

-- ============================================================
-- TIPOS DE IDENTIFICACIÓN
-- ============================================================
INSERT INTO identification_types (id, created_at, updated_at, created_by, updated_by, name, active)
VALUES
    ('f1e2d3c4-b5a6-4987-9876-543210fedcba', NOW(), NOW(), 'system', 'system', 'CÉDULA DE CIUDADANÍA', TRUE),
    ('e2d3c4b5-a6f7-4876-8765-43210fedcba9', NOW(), NOW(), 'system', 'system', 'PASAPORTE', TRUE);

-- ============================================================
-- TIPOS DE CRÉDITO
-- ============================================================
INSERT INTO loan_types (id, created_at, updated_at, created_by, updated_by, name, annual_rate, automatic_validation, active)
VALUES
    ('11111111-1111-1111-1111-111111111111', NOW(), NOW(), 'system', 'system', 'CRÉDITO DE CONSUMO', 18.5, TRUE, TRUE),
    ('22222222-2222-2222-2222-222222222222', NOW(), NOW(), 'system', 'system', 'CRÉDITO HIPOTECARIO', 12.0, FALSE, TRUE);

-- ============================================================
-- ESTADOS DE SOLICITUD (Corregidos a Hexadecimal)
-- ============================================================
INSERT INTO loan_request_statuses (id, created_at, updated_at, created_by, updated_by, name)
VALUES
    ('a1a1a1a1-1111-1111-1111-a1a1a1a1a1a1', NOW(), NOW(), 'system', 'system', 'PENDIENTE_REVISION'),
    ('b2b2b2b2-2222-2222-2222-b2b2b2b2b2b2', NOW(), NOW(), 'system', 'system', 'APROBADO'),
    ('c3c3c3c3-3333-3333-3333-c3c3c3c3c3c3', NOW(), NOW(), 'system', 'system', 'RECHAZADO');

-- ============================================================
-- ESTADOS DE CREDITOR
-- ============================================================
INSERT INTO loan_statuses (id, created_at, updated_at, created_by, updated_by, name)
VALUES
    ('a1a1a1a1-1111-1111-1111-a1a1a1a1a1a1', NOW(), NOW(), 'system', 'system', 'ACTIVO'),
    ('c3c3c3c3-3333-3333-3333-c3c3c3c3c3c3', NOW(), NOW(), 'system', 'system', 'FINALIZADO');

-- ============================================================
-- CLIENTES DE PRUEBA
-- ============================================================
INSERT INTO customers (id, created_at, updated_at, created_by, updated_by, user_id, identification_type_id, first_name, last_name, identification_number, base_salary)
VALUES
    ('99999999-9999-9999-9999-999999999999', NOW(), NOW(), 'system', 'system',
     '00000000-0000-0000-0000-000000000002',
     'f1e2d3c4-b5a6-4987-9876-543210fedcba',
     'JUAN', 'PEREZ', '123456789', 3500000.00);

-- ============================================================
-- SYSTEM ENTITIES (Módulos)
-- ============================================================
INSERT INTO system_entities (id, created_at, updated_at, created_by, updated_by, name, cod_group, name_group, path, name_to_view_client, "order")
VALUES
    ('e1e1e1e1-1111-1111-1111-e1e1e1e1e1e1', NOW(), NOW(), 'system', 'system', 'USER', 'SYS', 'System', '/api/user', 'Gestión de Usuarios', 1),
    ('e2e2e2e2-2222-2222-2222-e2e2e2e2e2e2', NOW(), NOW(), 'system', 'system', 'ROLE', 'SYS', 'System', '/api/role', 'Gestión de Roles', 2),
    ('e4e4e4e4-4444-4444-4444-e4e4e4e4e4e4', NOW(), NOW(), 'system', 'system', 'PERMISSION', 'SYS', 'System', '/api/permission', 'Permisos de Acceso', 3),
    ('e5e5e5e5-5555-5555-5555-e5e5e5e5e5e5', NOW(), NOW(), 'system', 'system', 'IDENTIFICATION_TYPE', 'SYS', 'System', '/api/identification-type', 'Tipos de Identificación', 4),
    ('e6e6e6e6-6666-6666-6666-e6e6e6e6e6e6', NOW(), NOW(), 'system', 'system', 'LOAN_TYPE', 'SYS', 'System', '/api/loan-type', 'Tipos de Créditos', 5),
    ('f7f7f7f7-7777-7777-7777-f7f7f7f7f7f7', NOW(), NOW(), 'system', 'system', 'LOAN_REQUEST_STATUS', 'SYS', 'System', '/api/loan-request-status', 'Estados de Solicitud', 6),
    ('f8f8f8f8-8888-8888-8888-f8f8f8f8f8f8', NOW(), NOW(), 'system', 'system', 'LOAN_REQUEST', 'CORE', 'Solicitudes de créditos', '/api/loan-request', 'Solicitudes de Crédito', 7),
    ('f9f9f9f9-9999-9999-9999-f9f9f9f9f9f9', NOW(), NOW(), 'system', 'system', 'LOAN', 'CORE', 'Créditos', '/api/loan', 'Créditos', 8),
    ('b9b9b9b9-9999-9999-9999-b9b9b9b9b9b9', NOW(), NOW(), 'system', 'system', 'CUSTOMER', 'CORE', 'Créditos', '/api/customer', 'Gestión de Clientes', 9);

-- ============================================================
-- PERMISOS
-- ============================================================

-- 1. PERMISOS PARA ADMIN (Control Total)
INSERT INTO permissions (id, role_id, entity_id, can_create, can_read, can_update, can_delete)
SELECT
    gen_random_uuid(),
    'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', -- ID de ADMIN
    e.id,
    TRUE, TRUE, TRUE, TRUE
FROM system_entities e;

-- 2. PERMISOS PARA ANALYST (Gestión Operativa)
-- El analista puede leer todo, pero solo crear/editar Clientes y Solicitudes de Crédito.
INSERT INTO permissions (id, role_id, entity_id, can_create, can_read, can_update, can_delete)
SELECT
    gen_random_uuid(),
    'b2c3d4e5-f6a7-4b6c-9d0e-1f2a3b4c5d6e', -- ID de ANALYST
    e.id,
    CASE
        WHEN e.name IN ('LOAN_REQUEST', 'CUSTOMER') THEN TRUE
        ELSE FALSE
    END, -- can_create
    TRUE, -- can_read (Permiso para ver todos los módulos)
    CASE
        WHEN e.name IN ('LOAN_REQUEST', 'CUSTOMER', 'USER') THEN TRUE
        ELSE FALSE
    END, -- can_update (Puede gestionar solicitudes y actualizar info de usuarios/clientes)
    FALSE -- can_delete (Un analista nunca debería borrar registros)
FROM system_entities e;

-- ============================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ============================================================

-- Procedimiento para validar solicitudes de préstamo
CREATE OR REPLACE PROCEDURE sp_validate_loan_request(
    IN p_customer_id UUID,
    IN p_loan_type_id UUID,
    IN p_amount NUMERIC,
    IN p_term_months INTEGER,
    INOUT p_status_id UUID DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_base_salary NUMERIC;
    v_annual_rate FLOAT;
    v_monthly_rate FLOAT;
    v_monthly_installment NUMERIC;
    v_indebtedness_capacity NUMERIC;
    v_status_name VARCHAR(50);
BEGIN
    -- 1. Obtener salario del cliente y tasa del préstamo
    SELECT base_salary INTO v_base_salary FROM customers WHERE id = p_customer_id;
    SELECT annual_rate INTO v_annual_rate FROM loan_types WHERE id = p_loan_type_id;

    -- 2. Calcular cuota mensual (Sistema Francés)
    v_monthly_rate := (v_annual_rate / 100) / 12;

    IF v_monthly_rate > 0 THEN
        v_monthly_installment := p_amount * (v_monthly_rate / (1 - POWER(1 + v_monthly_rate, -p_term_months)));
    ELSE
        v_monthly_installment := p_amount / p_term_months;
    END IF;

    -- 3. Capacidad disponible (Ejemplo: 40% del salario)
    v_indebtedness_capacity := v_base_salary * 0.40;

    -- 4. Lógica de Negocio solicitada
    -- RECHAZADO: Cuota mayor a capacidad
    IF v_monthly_installment > v_indebtedness_capacity THEN
        v_status_name := 'RECHAZADO';

    -- REVISIÓN MANUAL: Aprobado pero monto > 5 salarios
    ELSIF p_amount > (v_base_salary * 5) THEN
        v_status_name := 'PENDIENTE_REVISION';

    -- APROBADO: Cuota <= capacidad y monto <= 5 salarios
    ELSE
        v_status_name := 'APROBADO';
    END IF;

    -- 5. Asignar el ID del estado al parámetro de salida
    SELECT id INTO p_status_id
    FROM loan_request_statuses
    WHERE name = v_status_name;

    -- Si no existe el estado en la tabla, lanzamos error para evitar inconsistencias
    IF p_status_id IS NULL THEN
        RAISE EXCEPTION 'Estado % no encontrado en la tabla loan_request_statuses', v_status_name;
    END IF;
END;
$$;

-- ============================================================
-- FIN DEL SCRIPT DE INICIALIZACIÓN
-- ============================================================

