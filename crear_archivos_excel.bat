@echo off
echo ========================================
echo CREANDO ARCHIVOS EXCEL DE MANTENIMIENTO
echo ========================================
echo.

echo 🔧 Instalando dependencias...
pip install -r requirements.txt

echo.
echo 🔧 Ejecutando script de creación...
python crear_archivos_excel.py

echo.
echo ✅ ¡Proceso completado!
echo 📁 Los archivos se crearon en la carpeta 'archivos_mantenimiento'
echo.
pause 