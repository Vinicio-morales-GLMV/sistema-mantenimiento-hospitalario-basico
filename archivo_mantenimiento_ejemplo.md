# Archivo Excel de Mantenimiento - Estructura de Ejemplo

## 📋 ESTRUCTURA DEL ARCHIVO EXCEL

### **Hoja 1: Información General**
```
A1: REPORTE DE MANTENIMIENTO
A3: Equipo: [NOMBRE DEL EQUIPO]
A4: Código: [CÓDIGO DEL EQUIPO]
A5: Fecha de Mantenimiento: [FECHA]
A6: Técnico Responsable: [NOMBRE DEL TÉCNICO]
A7: Tipo de Mantenimiento: [PREVENTIVO/CORRECTIVO]
A8: Estado: [COMPLETADO/EN PROCESO/PENDIENTE]

A10: DESCRIPCIÓN DEL MANTENIMIENTO
A11: [DESCRIPCIÓN DETALLADA]
```

### **Hoja 2: Actividades Realizadas**
```
A1: ACTIVIDADES REALIZADAS
A3: No. | Actividad | Estado | Observaciones
A4: 1   | Limpieza de componentes | Completado | Se limpiaron todos los componentes principales
A5: 2   | Calibración de sensores | Completado | Sensores calibrados según especificaciones
A6: 3   | Verificación de funcionamiento | Completado | Equipo funciona correctamente
A7: 4   | Reemplazo de filtros | Completado | Filtros reemplazados por nuevos
A8: 5   | Lubricación de partes móviles | Completado | Lubricación aplicada según manual
```

### **Hoja 3: Materiales Utilizados**
```
A1: MATERIALES UTILIZADOS
A3: No. | Material | Cantidad | Costo Unitario | Costo Total
A4: 1   | Filtros de aire | 2 | $25.00 | $50.00
A5: 2   | Aceite lubricante | 1 | $15.00 | $15.00
A6: 3   | Paños de limpieza | 5 | $2.00 | $10.00
A7: 4   | Consumibles varios | 1 | $75.00 | $75.00
A8:     | TOTAL | | | $150.00
```

### **Hoja 4: Medidas de Seguridad**
```
A1: MEDIDAS DE SEGURIDAD APLICADAS
A3: No. | Medida | Aplicada | Observaciones
A4: 1   | Desconexión eléctrica | Sí | Equipo desconectado antes del mantenimiento
A5: 2   | Uso de EPP | Sí | Guantes y gafas utilizados
A6: 3   | Ventilación adecuada | Sí | Área bien ventilada
A7: 4   | Limpieza del área | Sí | Área limpiada después del trabajo
```

### **Hoja 5: Próximo Mantenimiento**
```
A1: PRÓXIMO MANTENIMIENTO
A3: Fecha Programada: [FECHA]
A4: Tipo: [PREVENTIVO/CORRECTIVO]
A5: Actividades Planificadas:
A6: - [ACTIVIDAD 1]
A7: - [ACTIVIDAD 2]
A8: - [ACTIVIDAD 3]
A9: Observaciones: [OBSERVACIONES]
```

## 📝 CONTENIDO DE EJEMPLO PARA CADA MANTENIMIENTO

### **Mantenimiento 1: AGITADOR CIRCULAR**
- **Descripción**: Mantenimiento preventivo mensual. Limpieza de componentes, calibración de sensores y verificación de funcionamiento.
- **Técnico**: Gabriel Leon
- **Costo**: $150.00
- **Actividades**: Limpieza, calibración, verificación
- **Materiales**: Paños, lubricante, filtros

### **Mantenimiento 2: AGITADOR VORTEX**
- **Descripción**: Reemplazo de filtros y lubricación de partes móviles. Verificación de presión y temperatura.
- **Técnico**: Santiago Nuñez
- **Costo**: $200.00
- **Actividades**: Reemplazo filtros, lubricación, verificación
- **Materiales**: Filtros nuevos, aceite lubricante

### **Mantenimiento 3: AGLUTINOSCOPIO**
- **Descripción**: Mantenimiento correctivo. Reparación de sistema de agitación y calibración de sensores.
- **Técnico**: Kerly Verdezoto
- **Costo**: $350.00
- **Actividades**: Reparación, calibración, pruebas
- **Materiales**: Repuestos, herramientas especializadas

## 🎯 PASOS PARA CREAR LOS ARCHIVOS

### **PASO 1: Crear bucket en Supabase**
1. Ve a tu dashboard de Supabase
2. Ve a **Storage**
3. Crea un bucket llamado **"mantenimientos"**
4. Configura permisos públicos para lectura

### **PASO 2: Crear archivos Excel**
1. Usa Excel o Google Sheets
2. Crea 25 archivos con la estructura de arriba
3. Nombra cada archivo como: `mnt_001.xlsx`, `mnt_002.xlsx`, etc.
4. Personaliza el contenido según cada mantenimiento

### **PASO 3: Subir archivos**
1. Ve al bucket "mantenimientos" en Supabase
2. Sube los 25 archivos Excel
3. Verifica que las URLs sean públicas

### **PASO 4: Actualizar base de datos**
1. Ejecuta el script `crear_bucket_mantenimientos.sql`
2. Verifica que las URLs se actualicen correctamente

## ✅ RESULTADO FINAL
- ✅ **25 archivos Excel** únicos
- ✅ **Información detallada** de cada mantenimiento
- ✅ **Estructura profesional** y consistente
- ✅ **Botones funcionales** en la app
- ✅ **Archivos descargables** desde la app 