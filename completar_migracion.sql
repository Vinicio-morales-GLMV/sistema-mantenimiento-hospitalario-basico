-- Script para completar la migración SIN recrear tablas existentes
-- Ejecutar en el SQL Editor de la nueva base de datos
-- Este script solo añade lo que falta

-- ========================================
-- 1. VERIFICAR QUÉ TABLAS EXISTEN
-- ========================================

-- Verificar si las tablas ya existen
SELECT 
    table_name,
    CASE 
        WHEN table_name = 'usuarios' THEN 'EXISTE'
        WHEN table_name = 'instrumentos' THEN 'EXISTE'
        WHEN table_name = 'mantenimientos' THEN 'EXISTE'
        ELSE 'NO EXISTE'
    END as estado
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name IN ('usuarios', 'instrumentos', 'mantenimientos');

-- ========================================
-- 2. CREAR TABLA DE MANTENIMIENTOS (SI NO EXISTE)
-- ========================================

-- Crear la tabla mantenimientos solo si no existe
CREATE TABLE IF NOT EXISTS mantenimientos (
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

-- Crear índices para mantenimientos (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_mantenimientos_equipo_id ON mantenimientos(equipo_id);
CREATE INDEX IF NOT EXISTS idx_mantenimientos_fecha ON mantenimientos(fecha_mantenimiento);
CREATE INDEX IF NOT EXISTS idx_mantenimientos_estado ON mantenimientos(estado);
CREATE INDEX IF NOT EXISTS idx_mantenimientos_tecnico ON mantenimientos(tecnico);

-- ========================================
-- 3. INSERTAR DATOS DE MANTENIMIENTOS
-- ========================================

-- Insertar datos de mantenimientos (solo si la tabla está vacía)
INSERT INTO mantenimientos (
    equipo_id,
    tipo_mantenimiento,
    descripcion,
    fecha_mantenimiento,
    proximo_mantenimiento,
    costo,
    tecnico,
    estado,
    observaciones,
    archivo_url
) 
SELECT * FROM (
    VALUES
    -- Mantenimientos para AGITADOR (EQ-001)
    (1, 'Preventivo', 'Limpieza y calibración del agitador. Verificación de velocidad y temperatura.', '2024-01-15', '2024-04-15', 150.00, 'Gabriel Leon', 'Completado', 'Equipo funcionando correctamente después del mantenimiento', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_agitador_001.xlsx'),
    (1, 'Correctivo', 'Reparación del motor del agitador. Reemplazo de rodamientos.', '2024-02-20', '2024-05-20', 300.00, 'Amy Aguagiña', 'Completado', 'Motor reparado exitosamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/reparacion_agitador_002.xlsx'),
    
    -- Mantenimientos para ANALIZADOR (EQ-004)
    (4, 'Preventivo', 'Calibración del analizador COBAS C111. Verificación de sensores y calibradores.', '2024-01-10', '2024-04-10', 200.00, 'Kerly Verdezoto', 'Completado', 'Analizador calibrado correctamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/calibracion_cobas_001.xlsx'),
    (4, 'Preventivo', 'Limpieza de tubos y verificación de flujo de reactivos.', '2024-03-05', '2024-06-05', 120.00, 'Amy Aguagiña', 'Pendiente', 'Programado para la próxima semana', NULL),
    
    -- Mantenimientos para AUTOCLAVE (EQ-011)
    (11, 'Preventivo', 'Verificación de presión y temperatura. Limpieza de cámaras.', '2024-01-20', '2024-04-20', 180.00, 'Gabriel Leon', 'Completado', 'Autoclave funcionando en parámetros normales', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_autoclave_001.xlsx'),
    (11, 'Correctivo', 'Reparación de válvula de seguridad. Reemplazo de sellos.', '2024-02-15', '2024-05-15', 450.00, 'Kerly Verdezoto', 'Completado', 'Válvula reemplazada exitosamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/reparacion_autoclave_002.xlsx'),
    
    -- Mantenimientos para AUDIOMETRO (EQ-010)
    (10, 'Preventivo', 'Calibración del audiómetro. Verificación de frecuencias y niveles de sonido.', '2024-01-25', '2024-04-25', 250.00, 'Gabriel Leon', 'Completado', 'Audiómetro calibrado correctamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/calibracion_audiometro_001.xlsx'),
    
    -- Mantenimientos para BALANZA (EQ-014)
    (14, 'Preventivo', 'Calibración de la balanza digital. Verificación de precisión.', '2024-02-01', '2024-05-01', 100.00, 'Amy Aguagiña', 'Completado', 'Balanza calibrada exitosamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/calibracion_balanza_001.xlsx'),
    
    -- Mantenimientos para BAÑO MARIA (EQ-016)
    (16, 'Preventivo', 'Limpieza del baño maría. Verificación de temperatura y circulación de agua.', '2024-02-10', '2024-05-10', 80.00, 'Amy Aguagiña', 'Completado', 'Equipo funcionando correctamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_banomaria_001.xlsx'),
    
    -- Mantenimientos para BICICLETA (EQ-017)
    (17, 'Preventivo', 'Limpieza y lubricación de la bicicleta de ejercicio. Verificación de resistencia.', '2024-02-05', '2024-05-05', 60.00, 'Amy Aguagiña', 'Completado', 'Bicicleta en buen estado', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_bicicleta_001.xlsx'),
    
    -- Mantenimientos para BOMBA DE INFUSION (EQ-019)
    (19, 'Preventivo', 'Verificación de flujo y presión de la bomba de infusión.', '2024-01-30', '2024-04-30', 200.00, 'Gabriel Leon', 'Completado', 'Bomba funcionando correctamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_bomba_001.xlsx'),
    (19, 'Correctivo', 'Reparación de sensor de presión. Reemplazo de componentes electrónicos.', '2024-03-01', '2024-06-01', 350.00, 'Kerly Verdezoto', 'En Proceso', 'Esperando repuestos', NULL),
    
    -- Mantenimientos para CABINA DE SEGURIDAD (EQ-020)
    (20, 'Preventivo', 'Verificación de filtros HEPA y flujo de aire de la cabina de seguridad.', '2024-02-15', '2024-05-15', 300.00, 'Gabriel Leon', 'Completado', 'Cabina funcionando correctamente', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mantenimiento_cabina_001.xlsx'),
    
    -- Mantenimientos programados para el futuro
    (2, 'Preventivo', 'Limpieza y calibración del agitador TM-1000.', '2024-04-01', '2024-07-01', 150.00, 'Amy Aguagiña', 'Pendiente', 'Programado para abril', NULL),
    (3, 'Preventivo', 'Verificación del aglutinador SBS España.', '2024-04-05', '2024-07-05', 120.00, 'Amy Aguagiña', 'Pendiente', 'Programado para abril', NULL),
    (5, 'Preventivo', 'Calibración del analizador SMART LYTE DIAMOND.', '2024-04-10', '2024-07-10', 200.00, 'Gabriel Leon', 'Pendiente', 'Programado para abril', NULL),
    (6, 'Preventivo', 'Mantenimiento del analizador HORIBA AMERICAN.', '2024-04-15', '2024-07-15', 180.00, 'Kerly Verdezoto', 'Pendiente', 'Programado para abril', NULL),
    (7, 'Preventivo', 'Verificación del arco dental.', '2024-04-20', '2024-07-20', 100.00, 'Amy Aguagiña', 'Pendiente', 'Programado para abril', NULL),
    (8, 'Preventivo', 'Limpieza del armónico ETHICON.', '2024-04-25', '2024-07-25', 80.00, 'Amy Aguagiña', 'Pendiente', 'Programado para abril', NULL),
    (9, 'Preventivo', 'Verificación del aspirador BAIRHUGGER.', '2024-05-01', '2024-08-01', 150.00, 'Gabriel Leon', 'Pendiente', 'Programado para mayo', NULL),
    (12, 'Preventivo', 'Mantenimiento del autoclave SERCON.', '2024-05-05', '2024-08-05', 180.00, 'Kerly Verdezoto', 'Pendiente', 'Programado para mayo', NULL),
    (13, 'Preventivo', 'Calibración del autorrefractómetro NIDEK.', '2024-05-10', '2024-08-10', 250.00, 'Gabriel Leon', 'Pendiente', 'Programado para mayo', NULL),
    (15, 'Preventivo', 'Limpieza del baño de parafina.', '2024-05-15', '2024-08-15', 90.00, 'Amy Aguagiña', 'Pendiente', 'Programado para mayo', NULL),
    (18, 'Preventivo', 'Verificación del bloque seco BENCHMARK.', '2024-05-20', '2024-08-20', 110.00, 'Amy Aguagiña', 'Pendiente', 'Programado para mayo', NULL)
) AS datos_mantenimientos (
    equipo_id INTEGER,
    tipo_mantenimiento VARCHAR(50),
    descripcion TEXT,
    fecha_mantenimiento DATE,
    proximo_mantenimiento DATE,
    costo DECIMAL(10,2),
    tecnico VARCHAR(100),
    estado VARCHAR(50),
    observaciones TEXT,
    archivo_url TEXT
)
WHERE NOT EXISTS (SELECT 1 FROM mantenimientos LIMIT 1);

-- ========================================
-- 4. VERIFICAR LA MIGRACIÓN COMPLETA
-- ========================================

-- Verificar que todas las tablas existen
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

-- Verificar mantenimientos con equipos
SELECT 
    m.id,
    i.nombre as equipo,
    m.tipo_mantenimiento,
    m.fecha_mantenimiento,
    m.estado,
    m.tecnico
FROM mantenimientos m 
LEFT JOIN instrumentos i ON m.equipo_id = i.id 
ORDER BY m.fecha_mantenimiento DESC
LIMIT 10;

-- Mostrar estadísticas de mantenimientos
SELECT 
    estado,
    COUNT(*) as cantidad,
    AVG(costo) as costo_promedio
FROM mantenimientos 
GROUP BY estado;

-- Mostrar mantenimientos por técnico
SELECT 
    tecnico,
    COUNT(*) as mantenimientos_realizados,
    AVG(costo) as costo_promedio
FROM mantenimientos 
WHERE tecnico IS NOT NULL
GROUP BY tecnico
ORDER BY mantenimientos_realizados DESC; 