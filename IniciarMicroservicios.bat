@echo off
set ROOT=%~dp0

start "servicio-clientes" cmd /k "cd /d "%ROOT%clientes\clientes" && call mvnw spring-boot:run"
start "servicio-compra" cmd /k "cd /d "%ROOT%compra\compra" && call mvnw spring-boot:run"
start "servicio-inventario" cmd /k "cd /d "%ROOT%inventario\inventario" && call mvnw spring-boot:run"
start "servicio-pagos" cmd /k "cd /d "%ROOT%pagos\pagos" && call mvnw spring-boot:run"
start "servicio-producto" cmd /k "cd /d "%ROOT%producto\producto" && call mvnw spring-boot:run"
timeout /t 15
start "servicio-gateway" cmd /k "cd /d "%ROOT%gateway\gateway" && call mvnw spring-boot:run"

echo Todos los servicios iniciados!