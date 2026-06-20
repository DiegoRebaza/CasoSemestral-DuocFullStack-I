@echo off
rem Inicia todos los microservicios del Caso Semestral, cada uno en su propia ventana

set ROOT=D:\FinalShit\CasoSemestral-DuocFullStack-I

start "auth" cmd /k "cd /d %ROOT%\auth && .\mvnw.cmd spring-boot:run"
timeout /t 8

start "clientes" cmd /k "cd /d %ROOT%\clientes\clientes && .\mvnw.cmd spring-boot:run"
start "inventario" cmd /k "cd /d %ROOT%\inventario\inventario && .\mvnw.cmd spring-boot:run"
start "producto" cmd /k "cd /d %ROOT%\producto\producto && .\mvnw.cmd spring-boot:run"
start "pagos" cmd /k "cd /d %ROOT%\pagos\pagos && .\mvnw.cmd spring-boot:run"
timeout /t 10

start "compra" cmd /k "cd /d %ROOT%\compra\compra && .\mvnw.cmd spring-boot:run"
start "recomendaciones" cmd /k "cd /d %ROOT%\recomendaciones && .\mvnw.cmd spring-boot:run"
timeout /t 10

start "gateway" cmd /k "cd /d %ROOT%\gateway\gateway && .\mvnw.cmd spring-boot:run"

echo.
echo Todos los microservicios se estan iniciando en ventanas separadas.
