-- Script para mostrar usuarios con credenciales y roles
-- Ejecutar en el SQL Editor de Supabase

-- ========================================
-- TABLA DE USUARIOS CON CREDENCIALES
-- ========================================

SELECT 
    'ID' as columna,
    'Email' as valor,
    'Contraseña' as password,
    'Nombre' as nombre,
    'Apellido' as apellido,
    'Rol' as rol,
    'Especialidad' as especialidad
UNION ALL
SELECT 
    '1' as columna,
    'santiago.nunez@hospitec.com' as valor,
    'santiago1234' as password,
    'Santiago' as nombre,
    'Nuñez' as apellido,
    'admin' as rol,
    'Todas las funciones e inventario' as especialidad
UNION ALL
SELECT 
    '2' as columna,
    'gabriel.leon@hospitec.com' as valor,
    'gabriel1234' as password,
    'Gabriel' as nombre,
    'Leon' as apellido,
    'admin' as rol,
    'Administrador, acceso total + puede mandar notificaciones, Ingeniero biomédico' as especialidad
UNION ALL
SELECT 
    '3' as columna,
    'kerly.verdezoto@hospitec.com' as valor,
    'kerly1234' as password,
    'Kerly' as nombre,
    'Verdezoto' as apellido,
    'admin' as rol,
    'Administrador, Ingeniera biomédica, puede alterar mantenimiento e inventario' as especialidad
UNION ALL
SELECT 
    '4' as columna,
    'amy.aguagina@hospitec.com' as valor,
    'amy1234' as password,
    'Amy' as nombre,
    'Aguagiña' as apellido,
    'tecnico' as rol,
    'Técnica de mantenimiento (funciones específicas a definir)' as especialidad
UNION ALL
SELECT 
    '5' as columna,
    'milena.silva@hospitec.com' as valor,
    'milena1234' as password,
    'Milena' as nombre,
    'Silva' as apellido,
    'doctor' as rol,
    'Doctora, solo puede poner si una máquina funciona o no' as especialidad
UNION ALL
SELECT 
    '6' as columna,
    'sebastian.salas@hospitec.com' as valor,
    'sebastian1234' as password,
    'Sebastian' as nombre,
    'Salas' as apellido,
    'usuario' as rol,
    'Usuario, solo puede ver el inventario' as especialidad;

-- ========================================
-- VERIFICAR USUARIOS EN LA BASE DE DATOS
-- ========================================

SELECT 
    id,
    email,
    nombre,
    apellido,
    rol,
    especialidad,
    telefono,
    activo
FROM usuarios 
ORDER BY id;

-- ========================================
-- RESUMEN DE ROLES
-- ========================================

SELECT 
    rol,
    COUNT(*) as cantidad_usuarios,
    STRING_AGG(nombre || ' ' || apellido, ', ') as usuarios
FROM usuarios 
GROUP BY rol
ORDER BY rol; 