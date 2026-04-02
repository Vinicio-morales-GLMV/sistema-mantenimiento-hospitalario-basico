-- Script de migración SIN RLS para nueva base de datos Supabase
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
-- 3. CREAR TABLA DE MANTENIMIENTOS
-- ========================================

-- Crear la tabla mantenimientos
CREATE TABLE mantenimientos (
    id SERIAL PRIMARY KEY,
    equipo_id INTEGER NOT NULL,
    tipo_mantenimiento VARCHAR(50),
    descripcion TEXT,
    fecha_mantenimiento DATE,
    proximo_mantenimiento DATE,
    costo DECIMAL(10,2),
    tecnico VARCHAR(100),
    estado VARCHAR(50) DEFAULT 'Pendiente',
    observaciones TEXT,
    archivo_url TEXT,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (equipo_id) REFERENCES instrumentos(id) ON DELETE CASCADE
);

-- Crear índices para mantenimientos
CREATE INDEX idx_mantenimientos_equipo_id ON mantenimientos(equipo_id);
CREATE INDEX idx_mantenimientos_fecha ON mantenimientos(fecha_mantenimiento);
CREATE INDEX idx_mantenimientos_estado ON mantenimientos(estado);
CREATE INDEX idx_mantenimientos_tecnico ON mantenimientos(tecnico);

-- ========================================
-- 4. INSERTAR DATOS DE PRUEBA
-- ========================================

-- Insertar usuarios reales de HospiTec con contraseñas "nombre1234"
INSERT INTO usuarios (email, password_hash, nombre, apellido, rol, especialidad, telefono) VALUES
('santiago.nunez@hospitec.com', '$2a$10$hash_santiago1234', 'Santiago', 'Nuñez', 'admin', 'Todas las funciones e inventario', '0991234567'),
('gabriel.leon@hospitec.com', '$2a$10$hash_gabriel1234', 'Gabriel', 'Leon', 'admin', 'Administrador, acceso total + puede mandar notificaciones, Ingeniero biomédico', '0992345678'),
('kerly.verdezoto@hospitec.com', '$2a$10$hash_kerly1234', 'Kerly', 'Verdezoto', 'admin', 'Administrador, Ingeniera biomédica, puede alterar mantenimiento e inventario', '0993456789'),
('amy.aguagina@hospitec.com', '$2a$10$hash_amy1234', 'Amy', 'Aguagiña', 'tecnico', 'Técnica de mantenimiento (funciones específicas a definir)', '0994567890'),
('milena.silva@hospitec.com', '$2a$10$hash_milena1234', 'Milena', 'Silva', 'doctor', 'Doctora, solo puede poner si una máquina funciona o no', '0995678901'),
('sebastian.salas@hospitec.com', '$2a$10$hash_sebastian1234', 'Sebastian', 'Salas', 'usuario', 'Usuario, solo puede ver el inventario', '0996789012');

-- Insertar los 20 dispositivos médicos reales de la tabla
INSERT INTO instrumentos (codigo, nombre, tipo, marca, modelo, serie, ubicacion, estado, fecha_adquisicion, proximo_mantenimiento) VALUES
('EQ-001', 'AGITADOR', 'ANALÍTICO', 'LW SCIENTIFIC', 'RTL-BLVD-24T1', '14100375', 'LABORATORIO CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-002', 'AGITADOR', 'ANALÍTICO', 'LW SCIENTIFIC', 'TM-1000', '1110896', 'LABORATORIO CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-003', 'AGLUTINOS', 'ANALÍTICO', 'SBS ESPAÑA', 'BAG-1', '15457/017', 'LABORATORIO DE INMUNOLOGÍA O HEMATOLOGÍA', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-004', 'ANALIZADC', 'ANALÍTICO', 'COBAS', 'C111', '1911', 'LABORATORIO DE ANÁLISIS CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-005', 'ANALIZADC', 'ANALÍTICO', 'SMART LYTE', 'DIAMOND', '10A00566', 'LABORATORIO DE ANÁLISIS CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-006', 'ANALIZADC', 'ANALÍTICO', 'HORIBAMERICAN', 'ABXMICROSES60', '204ESOH03901', 'LABORATORIO DE ANÁLISIS CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-007', 'ARCO DEN', 'APOYO', 'TONTARRA', 'N/A', 'N/A', 'CONSULTORIO ODONTOLOGÍCO O QUIROFANO CIRUGÍA MAXILAR', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-008', 'ARMONICO', 'TERAPÉUTI', 'ETHICON', 'N/A', '11111219422', 'CONSULTORIO ODONTOLOGÍCO O QUIROFANO CIRUGÍA MAXILAR', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-009', 'ASPIRADOF', 'TERAPÉUTI', 'BAIRHUGGER', '775', '57028', 'QUIRÓFANOS, EMERGENCIA, UCI', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-010', 'AUDIOMETI', 'DIAGNÓSTI', 'RESONANCE', 'R37 HF', 'R37A17E000313', 'CONSULTORIO AUDIOLOGIA O OTORRINOLOGÍA', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-011', 'AUTOCLAVI', 'APOYO', 'N/A', 'EA-640', 'D008-175-0070', 'CENTRALES DE ESTERILIZACIÓN', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-012', 'AUTOCLAVI', 'APOYO', 'SERCON', 'AHMC-6', '233151478', 'CENTRALES DE ESTERILIZACIÓN', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-013', 'AUTOREFR', 'DIAGNÓSTI', 'NIDEK', 'ARK-1A', '531251', 'OFTALMOLOGÍA', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-014', 'BALANZA D', 'APOYO', 'ADE', 'M320600', '1.6335E+11', 'CONSULTORIOS', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-015', 'BAÑO DE P', 'ANALÍTICO', 'WHITEHALL', 'PT-18-S', '15073072', 'LABORATORIO CLÍNICO O INVESTIGACIÓN', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-016', 'BAÑO MARI', 'ANALÍTICO', 'LW SCIENTIFIC', 'DSB-500D', '15010002', 'LABORATORIO CLÍNICO O INVESTIGACIÓN', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-017', 'BICICLETA', 'TERAPÉUTI', 'SCHWINN', 'SCHWINN 130', '100335PR015180115', 'REHABILITACIÓN O CARDIOLOGÍA', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-018', 'BLOQUE SE', 'ANALÍTICO', 'BENCHMARK', 'N/A', 'AS-BSH1-2093', 'LABORATORIO CLÍNICO', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-019', 'BOMBA DE', 'TERAPÉUTI', 'FRESENIUS', 'N/A', '21847917', 'UCI. QUIRÓFANOS', 'Funcional', '2024-01-01', '2025-01-01'),
('EQ-020', 'CABINA DE', 'ANALÍTICO', 'BIOBASE', 'BSC130011A2-X', 'BSC13A1712284', 'LABORATORIO DE MICROBIOLOGÍA', 'Funcional', '2024-01-01', '2025-01-01');

-- ========================================
-- 5. VERIFICAR LA MIGRACIÓN
-- ========================================

-- Verificar usuarios
SELECT COUNT(*) as total_usuarios FROM usuarios;
SELECT email, nombre, apellido, rol FROM usuarios ORDER BY id;

-- Verificar instrumentos
SELECT COUNT(*) as total_instrumentos FROM instrumentos;
SELECT codigo, nombre, estado FROM instrumentos ORDER BY codigo;

-- Verificar mantenimientos
SELECT COUNT(*) as total_mantenimientos FROM mantenimientos;
SELECT m.id, i.nombre as equipo, m.tipo_mantenimiento, m.fecha_mantenimiento, m.estado 
FROM mantenimientos m 
LEFT JOIN instrumentos i ON m.equipo_id = i.id 
ORDER BY m.fecha_mantenimiento DESC;

-- Mostrar resumen completo
SELECT 
    'usuarios' as tabla,
    COUNT(*) as total_registros
FROM usuarios
UNION ALL
SELECT 
    'instrumentos' as tabla,
    COUNT(*) as total_registros
FROM instrumentos
UNION ALL
SELECT 
    'mantenimientos' as tabla,
    COUNT(*) as total_registros
FROM mantenimientos; 