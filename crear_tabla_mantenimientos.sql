-- Script para crear la tabla mantenimientos en Supabase
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- CREAR TABLA MANTENIMIENTOS
-- ========================================

-- Crear tabla mantenimientos
CREATE TABLE IF NOT EXISTS mantenimientos (
    id SERIAL PRIMARY KEY,
    equipo_id INTEGER NOT NULL,
    fecha_mantenimiento VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    archivo_url TEXT,
    tipo_mantenimiento VARCHAR(100),
    tecnico_responsable VARCHAR(100),
    costo DECIMAL(10,2),
    estado VARCHAR(50) DEFAULT 'Completado',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_falla TIMESTAMP,
    fecha_reparacion TIMESTAMP,
    duracion_reparacion DECIMAL(6,2),
    tiempo_funcionamiento DECIMAL(8,2),
    FOREIGN KEY (equipo_id) REFERENCES instrumentos(id) ON DELETE CASCADE
);

-- Crear índices para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_mantenimientos_equipo_id ON mantenimientos(equipo_id);
CREATE INDEX IF NOT EXISTS idx_mantenimientos_fecha ON mantenimientos(fecha_mantenimiento);
CREATE INDEX IF NOT EXISTS idx_mantenimientos_tipo ON mantenimientos(tipo_mantenimiento);

-- ========================================
-- INSERTAR DATOS DE EJEMPLO
-- ========================================

-- Insertar 25 mantenimientos de ejemplo
INSERT INTO mantenimientos (equipo_id, fecha_mantenimiento, descripcion, archivo_url, tipo_mantenimiento, tecnico_responsable, costo, estado) VALUES
(1, '2024-01-15', 'Mantenimiento preventivo mensual. Limpieza de componentes, calibración de sensores y verificación de funcionamiento.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_001.xlsx', 'Preventivo', 'Gabriel Leon', 150.00, 'Completado'),
(2, '2024-01-20', 'Reemplazo de filtros y lubricación de partes móviles. Verificación de presión y temperatura.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_002.xlsx', 'Preventivo', 'Santiago Nuñez', 200.00, 'Completado'),
(3, '2024-02-05', 'Mantenimiento correctivo. Reparación de sistema de agitación y calibración de sensores.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_003.xlsx', 'Correctivo', 'Kerly Verdezoto', 350.00, 'Completado'),
(4, '2024-02-10', 'Actualización de software y calibración de analizadores. Verificación de precisión.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_004.xlsx', 'Preventivo', 'Amy Aguagiña', 180.00, 'Completado'),
(5, '2024-02-25', 'Mantenimiento preventivo trimestral. Limpieza profunda y reemplazo de consumibles.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_005.xlsx', 'Preventivo', 'Gabriel Leon', 280.00, 'Completado'),
(6, '2024-03-08', 'Reparación de sistema de refrigeración y calibración de termostatos.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_006.xlsx', 'Correctivo', 'Santiago Nuñez', 420.00, 'Completado'),
(7, '2024-03-15', 'Mantenimiento preventivo. Limpieza de lentes y calibración de sistema óptico.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_007.xlsx', 'Preventivo', 'Kerly Verdezoto', 160.00, 'Completado'),
(8, '2024-03-22', 'Reemplazo de piezas desgastadas y lubricación de mecanismos.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_008.xlsx', 'Preventivo', 'Amy Aguagiña', 190.00, 'Completado'),
(9, '2024-04-05', 'Mantenimiento correctivo. Reparación de sistema de succión y filtros.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_009.xlsx', 'Correctivo', 'Gabriel Leon', 380.00, 'Completado'),
(10, '2024-04-12', 'Calibración de sistema auditivo y verificación de frecuencias.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_010.xlsx', 'Preventivo', 'Santiago Nuñez', 220.00, 'Completado'),
(11, '2024-04-25', 'Mantenimiento preventivo semestral. Limpieza profunda y reemplazo de juntas.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_011.xlsx', 'Preventivo', 'Kerly Verdezoto', 320.00, 'Completado'),
(12, '2024-05-03', 'Actualización de software y calibración de sensores de presión.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_012.xlsx', 'Preventivo', 'Amy Aguagiña', 170.00, 'Completado'),
(13, '2024-05-10', 'Mantenimiento correctivo. Reparación de sistema de enfoque automático.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_013.xlsx', 'Correctivo', 'Gabriel Leon', 450.00, 'Completado'),
(14, '2024-05-18', 'Calibración de balanza y verificación de precisión de medición.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_014.xlsx', 'Preventivo', 'Santiago Nuñez', 140.00, 'Completado'),
(15, '2024-05-25', 'Mantenimiento preventivo. Limpieza de sistema de calefacción.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_015.xlsx', 'Preventivo', 'Kerly Verdezoto', 180.00, 'Completado'),
(16, '2024-06-05', 'Reemplazo de termostatos y calibración de temperatura.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_016.xlsx', 'Preventivo', 'Amy Aguagiña', 210.00, 'Completado'),
(17, '2024-06-12', 'Mantenimiento correctivo. Reparación de sistema de pedaleo.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_017.xlsx', 'Correctivo', 'Gabriel Leon', 290.00, 'Completado'),
(18, '2024-06-20', 'Limpieza de sistema de calefacción y verificación de temperatura.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_018.xlsx', 'Preventivo', 'Santiago Nuñez', 160.00, 'Completado'),
(19, '2024-06-28', 'Mantenimiento preventivo. Calibración de sistema de infusión.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_019.xlsx', 'Preventivo', 'Kerly Verdezoto', 240.00, 'Completado'),
(20, '2024-07-05', 'Mantenimiento preventivo trimestral. Limpieza de filtros HEPA y verificación de flujo de aire.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_020.xlsx', 'Preventivo', 'Amy Aguagiña', 380.00, 'Completado'),
(1, '2024-07-12', 'Mantenimiento preventivo mensual. Verificación de funcionamiento y limpieza.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_021.xlsx', 'Preventivo', 'Gabriel Leon', 150.00, 'Completado'),
(3, '2024-07-20', 'Reparación de sistema de calibración y actualización de software.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_022.xlsx', 'Correctivo', 'Santiago Nuñez', 520.00, 'Completado'),
(5, '2024-07-28', 'Mantenimiento preventivo. Limpieza de componentes y calibración.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_023.xlsx', 'Preventivo', 'Kerly Verdezoto', 200.00, 'Completado'),
(7, '2024-08-05', 'Reemplazo de lámpara y calibración de sistema óptico.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_024.xlsx', 'Preventivo', 'Amy Aguagiña', 180.00, 'Completado'),
(10, '2024-08-12', 'Mantenimiento correctivo. Reparación de sistema de audio y calibración.', 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_025.xlsx', 'Correctivo', 'Gabriel Leon', 410.00, 'Completado');

-- ========================================
-- VERIFICAR INSERCIÓN
-- ========================================

-- Contar total de mantenimientos
SELECT COUNT(*) as total_mantenimientos FROM mantenimientos;

-- Mostrar algunos mantenimientos de ejemplo
SELECT 
    m.id,
    m.equipo_id,
    i.nombre as equipo_nombre,
    m.fecha_mantenimiento,
    m.descripcion,
    m.tipo_mantenimiento,
    m.tecnico_responsable,
    m.costo,
    m.estado
FROM mantenimientos m
LEFT JOIN instrumentos i ON m.equipo_id = i.id
ORDER BY m.fecha_mantenimiento DESC
LIMIT 10; 