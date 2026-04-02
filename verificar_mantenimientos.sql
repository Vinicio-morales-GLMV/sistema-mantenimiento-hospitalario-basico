-- Script para verificar mantenimientos en Supabase
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- VERIFICAR MANTENIMIENTOS
-- ========================================

-- Contar total de mantenimientos
SELECT COUNT(*) as total_mantenimientos FROM mantenimientos;

-- Mostrar todos los mantenimientos con información del equipo
SELECT 
    m.id,
    m.equipo_id,
    i.nombre as equipo_nombre,
    m.fecha_mantenimiento,
    m.descripcion,
    m.archivo_url,
    m.tipo_mantenimiento,
    m.tecnico_responsable,
    m.costo,
    m.estado
FROM mantenimientos m
LEFT JOIN instrumentos i ON m.equipo_id = i.id
ORDER BY m.fecha_mantenimiento DESC;

-- Contar mantenimientos por tipo
SELECT 
    tipo_mantenimiento,
    COUNT(*) as cantidad
FROM mantenimientos 
WHERE tipo_mantenimiento IS NOT NULL
GROUP BY tipo_mantenimiento 
ORDER BY cantidad DESC;

-- Contar mantenimientos por técnico
SELECT 
    tecnico_responsable,
    COUNT(*) as cantidad
FROM mantenimientos 
WHERE tecnico_responsable IS NOT NULL
GROUP BY tecnico_responsable 
ORDER BY cantidad DESC;

-- Mantenimientos con archivos adjuntos
SELECT 
    COUNT(*) as mantenimientos_con_archivos
FROM mantenimientos 
WHERE archivo_url IS NOT NULL AND archivo_url != '';

-- Mantenimientos sin archivos
SELECT 
    COUNT(*) as mantenimientos_sin_archivos
FROM mantenimientos 
WHERE archivo_url IS NULL OR archivo_url = '';

-- Resumen de costos
SELECT 
    COUNT(*) as total_mantenimientos,
    SUM(costo) as costo_total,
    AVG(costo) as costo_promedio,
    MIN(costo) as costo_minimo,
    MAX(costo) as costo_maximo
FROM mantenimientos 
WHERE costo IS NOT NULL;

-- Mantenimientos por mes (últimos 6 meses)
SELECT 
    DATE_TRUNC('month', fecha_mantenimiento::date) as mes,
    COUNT(*) as cantidad
FROM mantenimientos 
WHERE fecha_mantenimiento >= CURRENT_DATE - INTERVAL '6 months'
GROUP BY DATE_TRUNC('month', fecha_mantenimiento::date)
ORDER BY mes DESC; 