@echo off
rem Detiene todos los procesos Java (microservicios Spring Boot) en ejecucion

echo Deteniendo todos los microservicios...
taskkill /F /IM java.exe /T

echo.
echo Listo. Todos los procesos Java fueron detenidos.
pause
