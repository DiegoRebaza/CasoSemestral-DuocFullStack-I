@echo off
rem Compila todos los microservicios del Caso Semestral

cd ..\CasoSemestral-DuocFullStack-I

cd auth
call .\mvnw.cmd clean package -DskipTests

cd ..\clientes
call .\mvnw.cmd clean package -DskipTests

cd ..\..\compra
call .\mvnw.cmd clean package -DskipTests

cd ..\..\gateway
call .\mvnw.cmd clean package -DskipTests

cd ..\..\inventario
call .\mvnw.cmd clean package -DskipTests

cd ..\..\pagos
call .\mvnw.cmd clean package -DskipTests

cd ..\..\producto
call .\mvnw.cmd clean package -DskipTests

cd ..\..\recomendaciones
call .\mvnw.cmd clean package -DskipTests

cd ..\..\posventa
call .\mvnw.cmd clean package -DskipTests

cd ..\..\envios
call .\mvnw.cmd clean package -DskipTests

cd ..\..\notificacion
call .\mvnw.cmd clean package -DskipTests

cd ..\..\fidelizacion
call .\mvnw.cmd clean package -DskipTests

echo.
echo Compilacion finalizada para todos los microservicios.
pause
