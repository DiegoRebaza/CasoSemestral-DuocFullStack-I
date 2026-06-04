@echo off
echo Deteniendo microservicios...
taskkill /FI "WINDOWTITLE eq servicio-clientes*" /F
taskkill /FI "WINDOWTITLE eq servicio-compra*" /F
taskkill /FI "WINDOWTITLE eq servicio-inventario*" /F
taskkill /FI "WINDOWTITLE eq servicio-pagos*" /F
taskkill /FI "WINDOWTITLE eq servicio-producto*" /F
taskkill /FI "WINDOWTITLE eq servicio-gateway*" /F
echo Todos los servicios detenidos!
pause