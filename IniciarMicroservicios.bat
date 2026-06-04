@echo off
echo ========================================
echo   Iniciando microservicios Bravatta...
echo ========================================

echo.
echo [1/6] Iniciando Gateway (puerto 9090)...
start "Gateway" cmd /k "cd /d "%~dp0gateway" && .\mvnw spring-boot:run"
timeout /t 10 /nobreak > nul

echo [2/6] Iniciando Clientes (puerto 9093)...
start "Clientes" cmd /k "cd /d "%~dp0clientes" && .\mvnw spring-boot:run"
timeout /t 5 /nobreak > nul

echo [3/6] Iniciando Producto (puerto 9095)...
start "Producto" cmd /k "cd /d "%~dp0producto" && .\mvnw spring-boot:run"
timeout /t 5 /nobreak > nul

echo [4/6] Iniciando Inventario (puerto 9092)...
start "Inventario" cmd /k "cd /d "%~dp0inventario" && .\mvnw spring-boot:run"
timeout /t 5 /nobreak > nul

echo [5/6] Iniciando Pagos (puerto 9094)...
start "Pagos" cmd /k "cd /d "%~dp0pagos" && .\mvnw spring-boot:run"
timeout /t 5 /nobreak > nul

echo [6/6] Iniciando Compra (puerto 9091)...
start "Compra" cmd /k "cd /d "%~dp0compra" && .\mvnw spring-boot:run"

echo.
echo ========================================
echo   Todos los servicios iniciados.
echo   Revisa cada ventana para confirmar.
echo ========================================
pause