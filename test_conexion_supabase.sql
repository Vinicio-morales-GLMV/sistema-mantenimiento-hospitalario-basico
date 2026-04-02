-- Test 1: Verificar que podemos leer la tabla usuarios
SELECT COUNT(*) as total_usuarios FROM usuarios;

-- Test 2: Verificar estructura de la tabla
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'usuarios' 
ORDER BY ordinal_position;

-- Test 3: Verificar políticas RLS
SELECT schemaname, tablename, policyname, permissive, roles, cmd 
FROM pg_policies 
WHERE tablename = 'usuarios';

-- Test 4: Intentar insertar un usuario de prueba (esto fallará si hay RLS)
INSERT INTO usuarios (nombre, email, password_hash, rol) 
VALUES ('Test User', 'test@example.com', 'test123', 'usuario')
ON CONFLICT (email) DO NOTHING;

-- Test 5: Verificar si el usuario de prueba se insertó
SELECT * FROM usuarios WHERE email = 'test@example.com';

-- Test 6: Limpiar usuario de prueba
DELETE FROM usuarios WHERE email = 'test@example.com'; 