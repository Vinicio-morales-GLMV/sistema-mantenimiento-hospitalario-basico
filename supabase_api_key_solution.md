# SOLUCIÓN CON API KEY ESPECÍFICA

## Opción más segura: Crear API Key con permisos específicos

### 1. En Supabase Dashboard:
- Ve a Settings > API
- Crea una nueva API Key con permisos específicos
- Configura solo los permisos necesarios para equipos

### 2. Ventajas de esta opción:
✅ Mantiene RLS habilitado
✅ Seguridad controlada
✅ Permite solo operaciones específicas
✅ No afecta otras funciones

### 3. Configuración recomendada:
- **SELECT**: Permitir lectura de equipos
- **UPDATE**: Permitir actualización de estado
- **INSERT**: Permitir inserción de mantenimientos
- **DELETE**: No permitir (por seguridad)

### 4. Implementación en código:
```kotlin
// Usar la nueva API key en lugar de la actual
.addHeader("apikey", "TU_NUEVA_API_KEY_ESPECIFICA")
```

## ¿Cuál prefieres?

1. **Opción A**: Deshabilitar RLS (más simple, menos seguro)
2. **Opción B**: RLS configurado correctamente (más seguro)
3. **Opción C**: API Key específica (más seguro, más trabajo)

**Recomendación**: Opción B (RLS configurado) - mantiene seguridad y resuelve el problema. 