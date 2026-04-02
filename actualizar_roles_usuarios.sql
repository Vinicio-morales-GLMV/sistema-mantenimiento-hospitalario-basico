-- Script para actualizar específicamente Santiago Nuñez como jefe del hospital
-- Ejecutar en Supabase SQL Editor

-- Actualizar Santiago Nuñez como jefe del hospital (corregido)
UPDATE usuarios 
SET rol = 'jefe_hospital' 
WHERE nombre = 'Santiago' AND email = 'santiago.nunez@hospitec.com';

-- Si el anterior no funciona, intentar con el nombre completo
UPDATE usuarios 
SET rol = 'jefe_hospital' 
WHERE nombre = 'Santiago Nuñez' AND email = 'santiago.nunez@hospitec.com';

-- Verificar el estado actual de Santiago
SELECT id, nombre, email, rol FROM usuarios WHERE email = 'santiago.nunez@hospitec.com';

-- Verificar todos los cambios
SELECT id, nombre, email, rol FROM usuarios ORDER BY id; 