-- Script para crear bucket de almacenamiento para mantenimientos
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- CREAR BUCKET DE ALMACENAMIENTO
-- ========================================

-- Crear bucket para archivos de mantenimiento
-- Nota: Esto se hace desde la interfaz de Supabase Storage, no desde SQL
-- Ve a Storage en tu dashboard de Supabase y crea un bucket llamado "mantenimientos"

-- ========================================
-- ACTUALIZAR URLs CON ARCHIVOS REALES
-- ========================================

-- Actualizar mantenimientos con URLs de archivos reales
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_001.xlsx' WHERE id = 1;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_002.xlsx' WHERE id = 2;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_003.xlsx' WHERE id = 3;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_004.xlsx' WHERE id = 4;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_005.xlsx' WHERE id = 5;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_006.xlsx' WHERE id = 6;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_007.xlsx' WHERE id = 7;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_008.xlsx' WHERE id = 8;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_009.xlsx' WHERE id = 9;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_010.xlsx' WHERE id = 10;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_011.xlsx' WHERE id = 11;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_012.xlsx' WHERE id = 12;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_013.xlsx' WHERE id = 13;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_014.xlsx' WHERE id = 14;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_015.xlsx' WHERE id = 15;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_016.xlsx' WHERE id = 16;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_017.xlsx' WHERE id = 17;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_018.xlsx' WHERE id = 18;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_019.xlsx' WHERE id = 19;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_020.xlsx' WHERE id = 20;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_021.xlsx' WHERE id = 21;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_022.xlsx' WHERE id = 22;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_023.xlsx' WHERE id = 23;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_024.xlsx' WHERE id = 24;
UPDATE mantenimientos SET archivo_url = 'https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/mnt_025.xlsx' WHERE id = 25;

-- ========================================
-- VERIFICAR ACTUALIZACIÓN
-- ========================================

-- Contar mantenimientos con archivos
SELECT 
    COUNT(*) as total_mantenimientos,
    COUNT(archivo_url) as con_archivos,
    COUNT(*) - COUNT(archivo_url) as sin_archivos
FROM mantenimientos;

-- Mostrar algunos mantenimientos con archivos
SELECT 
    id,
    equipo_id,
    fecha_mantenimiento,
    descripcion,
    archivo_url,
    tipo_mantenimiento,
    tecnico_responsable
FROM mantenimientos 
WHERE archivo_url IS NOT NULL
ORDER BY id
LIMIT 5; 