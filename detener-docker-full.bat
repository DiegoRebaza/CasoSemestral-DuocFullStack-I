@echo off
echo ===================================
echo  Deteniendo contenedores y BORRANDO datos de MySQL
echo  (esto elimina el volumen, las bases de datos se reinician vacias)
echo ===================================
set /p confirm="Estas seguro? Esto borra TODOS los datos guardados (s/n): "
if /i "%confirm%" neq "s" goto :cancelado

docker compose down -v
echo Listo. Volumen de MySQL eliminado.
goto :fin

:cancelado
echo Operacion cancelada, no se borro nada.

:fin