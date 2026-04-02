-- Script para actualizar mantenimientos sin URLs de archivos
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- ACTUALIZAR MANTENIMIENTOS SIN ARCHIVOS
-- ========================================

-- Actualizar todos los mantenimientos para quitar las URLs de archivos
UPDATE mantenimientos 
SET archivo_url = NULL 
WHERE archivo_url IS NOT NULL;

-- Verificar que se actualizaron correctamente
SELECT 
    id,
    equipo_id,
    fecha_mantenimiento,
    descripcion,
    archivo_url,
    tipo_mantenimiento,
    tecnico_responsable,
    costo,
    estado
FROM mantenimientos 
ORDER BY fecha_mantenimiento DESC
LIMIT 10;

-- Contar mantenimientos con y sin archivos
SELECT 
    COUNT(*) as total_mantenimientos,
    COUNT(archivo_url) as con_archivos,
    COUNT(*) - COUNT(archivo_url) as sin_archivos
FROM mantenimientos; 