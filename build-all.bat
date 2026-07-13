@echo off
rem Compila todos los microservicios del Caso Semestral

D:\FinalShitAntesPostVenta\CasoSemestral-DuocFullStack-I

cd auth
call .\mvnw.cmd clean package -DskipTests

cd ..\clientes\clientes
call .\mvnw.cmd clean package -DskipTests

cd ..\..\compra\compra
call .\mvnw.cmd clean package -DskipTests

cd ..\..\gateway\gateway
call .\mvnw.cmd clean package -DskipTests

cd ..\..\inventario\inventario
call .\mvnw.cmd clean package -DskipTests

cd ..\..\pagos\pagos
call .\mvnw.cmd clean package -DskipTests

cd ..\..\producto\producto
call .\mvnw.cmd clean package -DskipTests

cd ..\..\recomendaciones
call .\mvnw.cmd clean package -DskipTests

cd ..\posventa
call .\mvnw.cmd clean package -DskipTests

cd ..\delivery
call .\mvnw.cmd clean package -DskipTests

echo.
echo Compilacion finalizada para todos los microservicios.
pause
