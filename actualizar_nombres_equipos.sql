-- Script para actualizar los nombres de equipos en Supabase
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- ACTUALIZAR NOMBRES DE EQUIPOS
-- ========================================

-- Actualizar nombres para que sean más completos y descriptivos
UPDATE instrumentos SET nombre = 'AGITADOR CIRCULAR VIBRATORIO PARA TUBOS' WHERE codigo = 'EQ-001';
UPDATE instrumentos SET nombre = 'AGITADOR TIPO VORTEX PARA MICROTUBOS' WHERE codigo = 'EQ-002';
UPDATE instrumentos SET nombre = 'AGLUTINOSCOPIO PARA ANÁLISIS DE SANGRE' WHERE codigo = 'EQ-003';
UPDATE instrumentos SET nombre = 'ANALIZADOR DE BIOQUÍMICA AUTOMATIZADO' WHERE codigo = 'EQ-004';
UPDATE instrumentos SET nombre = 'ANALIZADOR DE ELECTROLITOS DIAMOND' WHERE codigo = 'EQ-005';
UPDATE instrumentos SET nombre = 'ANALIZADOR HEMATOLÓGICO MICROS ES 60' WHERE codigo = 'EQ-006';
UPDATE instrumentos SET nombre = 'ARCO DENTAL ASCENSOR CIGOMÁTICO' WHERE codigo = 'EQ-007';
UPDATE instrumentos SET nombre = 'ARMÓNICO ULTRASÓNICO PARA CIRUGÍA' WHERE codigo = 'EQ-008';
UPDATE instrumentos SET nombre = 'ASPIRADOR DE FLUIDOS QUIRÚRGICOS' WHERE codigo = 'EQ-009';
UPDATE instrumentos SET nombre = 'AUDIÓMETRO CLÍNICO DE DIAGNÓSTICO' WHERE codigo = 'EQ-010';
UPDATE instrumentos SET nombre = 'AUTOCLAVE DE MESA AUTOMÁTICO' WHERE codigo = 'EQ-011';
UPDATE instrumentos SET nombre = 'AUTOCLAVE DE SOBREMESA 20-30 LITROS' WHERE codigo = 'EQ-012';
UPDATE instrumentos SET nombre = 'AUTORREFRACTOMETRO AUTOMÁTICO' WHERE codigo = 'EQ-013';
UPDATE instrumentos SET nombre = 'BALANZA DE PISO CON TALLÍMETRO' WHERE codigo = 'EQ-014';
UPDATE instrumentos SET nombre = 'BAÑO DE PARAFINA PORTÁTIL' WHERE codigo = 'EQ-015';
UPDATE instrumentos SET nombre = 'BAÑO MARÍA DIGITAL TEMPERATURA' WHERE codigo = 'EQ-016';
UPDATE instrumentos SET nombre = 'BICICLETA ESTÁTICA DE EJERCICIO' WHERE codigo = 'EQ-017';
UPDATE instrumentos SET nombre = 'BLOQUE SECO PARA TUBOS DE ENSAYO' WHERE codigo = 'EQ-018';
UPDATE instrumentos SET nombre = 'BOMBA DE INFUSIÓN PERISTÁLTICA' WHERE codigo = 'EQ-019';
UPDATE instrumentos SET nombre = 'CABINA DE SEGURIDAD BIOLÓGICA CLASE II' WHERE codigo = 'EQ-020';

-- ========================================
-- VERIFICAR ACTUALIZACIONES
-- ========================================

-- Mostrar todos los equipos con sus nombres actualizados
SELECT codigo, nombre, tipo, marca, modelo, ubicacion, estado 
FROM instrumentos 
ORDER BY codigo;

-- Contar equipos por tipo
SELECT tipo, COUNT(*) as cantidad
FROM instrumentos 
GROUP BY tipo 
ORDER BY cantidad DESC;

-- Mostrar equipos por ubicación
SELECT ubicacion, COUNT(*) as cantidad
FROM instrumentos 
GROUP BY ubicacion 
ORDER BY cantidad DESC; 