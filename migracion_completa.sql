-- Script completo de migración para nueva base de datos Supabase
-- Ejecutar en el SQL Editor de la nueva base de datos

-- ========================================
-- 1. CREAR TABLA DE USUARIOS
-- ========================================

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    rol VARCHAR(50) DEFAULT 'usuario',
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT NOW(),
    ultimo_acceso TIMESTAMP,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Crear índices para usuarios
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);

-- ========================================
-- 2. CREAR TABLA DE INSTRUMENTOS
-- ========================================

-- Crear la tabla instrumentos
CREATE TABLE instrumentos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(100),
    marca VARCHAR(100),
    modelo VARCHAR(100),
    serie VARCHAR(100),
    ubicacion VARCHAR(255),
    estado VARCHAR(50) DEFAULT 'Funcionando',
    fecha_adquisicion DATE,
    proximo_mantenimiento DATE,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    last_modified_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Crear índices para instrumentos
CREATE INDEX idx_instrumentos_codigo ON instrumentos(codigo);
CREATE INDEX idx_instrumentos_estado ON instrumentos(estado);
CREATE INDEX idx_instrumentos_ubicacion ON instrumentos(ubicacion);

-- ========================================
-- 3. INSERTAR DATOS DE PRUEBA
-- ========================================

-- Insertar usuarios de prueba
INSERT INTO usuarios (email, password_hash, nombre, apellido, rol, especialidad, telefono) VALUES
('admin@clinica.com', '$2a$10$ejemplo_hash_admin', 'Administrador', 'Sistema', 'admin', 'Administración', '0991234567'),
('doctor@clinica.com', '$2a$10$ejemplo_hash_doctor', 'Dr. Juan', 'Pérez', 'doctor', 'Cardiología', '0992345678'),
('enfermera@clinica.com', '$2a$10$ejemplo_hash_enfermera', 'Lic. María', 'González', 'enfermera', 'Enfermería', '0993456789'),
('tecnico@clinica.com', '$2a$10$ejemplo_hash_tecnico', 'Téc. Carlos', 'Rodríguez', 'tecnico', 'Mantenimiento', '0994567890');

-- Insertar instrumentos de prueba
INSERT INTO instrumentos (codigo, nombre, tipo, marca, modelo, serie, ubicacion, estado, fecha_adquisicion, proximo_mantenimiento) VALUES
('INS001', 'Monitor de Signos Vitales', 'Monitor', 'Philips', 'IntelliVue MX40', 'MX40-001', 'Sala de Emergencias', 'Funcionando', '2023-01-15', '2024-06-15'),
('INS002', 'Desfibrilador', 'Desfibrilador', 'ZOLL', 'AED Plus', 'ZOLL-002', 'Sala de Emergencias', 'Funcionando', '2023-02-20', '2024-07-20'),
('INS003', 'Ventilador Mecánico', 'Ventilador', 'Dräger', 'Evita V500', 'DRG-003', 'UCI', 'En mantenimiento', '2023-03-10', '2024-08-10'),
('INS004', 'Bomba de Infusión', 'Bomba', 'B. Braun', 'Infusomat', 'BB-004', 'UCI', 'Funcionando', '2023-04-05', '2024-09-05'),
('INS005', 'Electrocardiógrafo', 'ECG', 'GE Healthcare', 'MAC 2000', 'GE-005', 'Cardiología', 'Sin funcionar', '2023-05-12', '2024-10-12'),
('INS006', 'Ultrasonido', 'Ultrasonido', 'Siemens', 'Acuson X300', 'SIE-006', 'Radiología', 'Funcionando', '2023-06-18', '2024-11-18'),
('INS007', 'Endoscopio', 'Endoscopio', 'Olympus', 'GIF-H290', 'OLY-007', 'Gastroenterología', 'Funcionando', '2023-07-25', '2024-12-25'),
('INS008', 'Lámpara Quirúrgica', 'Iluminación', 'Trumpf', 'LED 1000', 'TRU-008', 'Quirófano 1', 'Funcionando', '2023-08-30', '2025-01-30'),
('INS009', 'Mesa Quirúrgica', 'Mesa', 'Maquet', 'Alphamaquet 1150', 'MAQ-009', 'Quirófano 2', 'En mantenimiento', '2023-09-14', '2025-02-14'),
('INS010', 'Anestesia Machine', 'Anestesia', 'Datex-Ohmeda', 'Aisys CS2', 'DAT-010', 'Quirófano 3', 'Funcionando', '2023-10-22', '2025-03-22');

-- ========================================
-- 4. CONFIGURAR RLS (Row Level Security)
-- ========================================

-- Habilitar RLS en ambas tablas
ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE instrumentos ENABLE ROW LEVEL SECURITY;

-- Políticas para usuarios
CREATE POLICY "Allow anonymous read access to usuarios" ON usuarios
    FOR SELECT USING (true);

CREATE POLICY "Allow anonymous insert access to usuarios" ON usuarios
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Allow anonymous update access to usuarios" ON usuarios
    FOR UPDATE USING (true);

-- Políticas para instrumentos
CREATE POLICY "Allow anonymous read access to instrumentos" ON instrumentos
    FOR SELECT USING (true);

CREATE POLICY "Allow anonymous update access to instrumentos" ON instrumentos
    FOR UPDATE USING (true);

-- ========================================
-- 5. VERIFICAR LA MIGRACIÓN
-- ========================================

-- Verificar usuarios
SELECT COUNT(*) as total_usuarios FROM usuarios;
SELECT email, nombre, apellido, rol FROM usuarios ORDER BY id;

-- Verificar instrumentos
SELECT COUNT(*) as total_instrumentos FROM instrumentos;
SELECT codigo, nombre, estado FROM instrumentos ORDER BY codigo;

-- Mostrar resumen
SELECT 
    'usuarios' as tabla,
    COUNT(*) as total_registros
FROM usuarios
UNION ALL
SELECT 
    'instrumentos' as tabla,
    COUNT(*) as total_registros
FROM instrumentos; 