@echo off
echo ===================================
echo  Creando preimagenes de los contenedores 
echo ===================================
docker compose build --parallel=false
