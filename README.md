# 🍦 Tienda de Helados Bravatta 🍦

Sistema de gestión y venta de helados desarrollado con arquitectura de microservicios usando Spring Boot.

---

## Integrantes

| Nombre |
|---|
| Diego Rebaza | 
| Matias Pavez |

**Repositorio:** https://github.com/DiegoRebaza/CasoSemestral-DuocFullStack-I

---

## Arquitectura General

```
[Frontend - HTML/CSS/JS]  ←→          [BFF]
                                        ↓
                              [API Gateway :8080]
                                        ↓
        ┌──────────┬──────────┬─────────┼─────────┬──────────┬──────────┬──────────┬──────────┬─────────────┐
        ↓          ↓          ↓         ↓         ↓          ↓          ↓          ↓          ↓             ↓
     [auth]   [clientes]  [compra] [inventario] [pagos] [producto] [recomend.] [posventa]  [envios]   [notificacion]
     :9080     :9081       :9082     :9083       :9084    :9085      :9086       :9087      :9088        :9089
        ↓          ↓          ↓         ↓         ↓          ↓          ↓          ↓          ↓
   [bd_users] [bd_cliente] [bd_compras][bd_inv] [bd_pagos][bd_prod] [bd_recom.] [bd_posv.] [bd_envios] [bd_notifi.]
```

## Microservicios

### 🔐 auth — Puerto 9080
Gestión de autenticación de usuarios. Registro, login y generación de tokens JWT.

**Base de datos:** `bd_users`

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión, retorna JWT |
| POST | `/auth/register` | Registrar nuevo usuario |

---

### 👤 clientes — Puerto 9081
Gestión de clientes y sus direcciones.

**Base de datos:** `bd_cliente`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/clientes` | Listar todos los clientes |
| GET | `/api/clientes/{id}` | Obtener cliente por ID |
| GET | `/api/clientes/{id}/exists` | Verificar existencia de cliente |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

---

### 🛒 compra — Puerto 9082
Gestión de compras y sus detalles. Se comunica con `clientes` y `producto` para validaciones.

**Base de datos:** `bd_compras`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/compras` | Listar todas las compras |
| GET | `/api/compras/{id}` | Obtener compra por ID |
| GET | `/api/compras/{id}/exists` | Verificar existencia de compra |
| GET | `/api/compras/buscar/fechas?inicio=&fin=` | Buscar compras por rango de fechas |
| GET | `/api/compras/total-ventas` | Total acumulado de ventas |
| POST | `/api/compras` | Crear compra |
| PUT | `/api/compras/{id}` | Actualizar compra |
| DELETE | `/api/compras/{id}` | Eliminar compra |

---

### 📦 inventario — Puerto 9083
Control de stock de productos. Se comunica con `producto` para validar existencia.

**Base de datos:** `bd_inventario`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/inventario` | Listar todo el inventario |
| GET | `/api/inventario/{id}` | Obtener inventario por ID |
| GET | `/api/inventario/stock-bajo/{cantidad}` | Productos con stock bajo el umbral |
| POST | `/api/inventario` | Registrar inventario de producto nuevo |
| PUT | `/api/inventario/{id}` | Actualizar inventario |
| PUT | `/api/inventario/descontar/{productoId}?cantidad=` | Descontar stock |
| DELETE | `/api/inventario/{id}` | Eliminar registro de inventario |

---

### 💳 pagos — Puerto 9084
Gestión de transacciones de pago.

**Base de datos:** `bd_pagos`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/pagos` | Listar todos los pagos |
| GET | `/api/pagos/{id}` | Obtener pago por ID |
| POST | `/api/pagos` | Registrar pago |
| PUT | `/api/pagos/{id}` | Actualizar pago |
| DELETE | `/api/pagos/{id}` | Eliminar pago |

---

### 🍦 producto — Puerto 9085
Catálogo de productos (helados).

**Base de datos:** `bd_producto`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/producto` | Listar todos los productos |
| GET | `/api/producto/{id}` | Obtener producto por ID |
| GET | `/api/producto/{id}/exists` | Verificar existencia de producto |
| POST | `/api/producto` | Crear producto |
| PUT | `/api/producto/{id}` | Actualizar producto |
| DELETE | `/api/producto/{id}` | Eliminar producto |

---

### ⭐ recomendaciones — Puerto 9086
Permite a los usuarios crear y consultar recomendaciones de productos.

**Base de datos:** `bd_recomendaciones`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/recomendaciones` | Listar recomendaciones |
| GET | `/api/recomendaciones/{id}` | Obtener recomendación por ID |
| POST | `/api/recomendaciones` | Crear recomendación |
| PUT | `/api/recomendaciones/{id}` | Actualizar recomendación |
| DELETE | `/api/recomendaciones/{id}` | Eliminar recomendación |

---

### 🛠️ posventa — Puerto 9087
Sistema de reportes de fallas o problemas con pedidos.

**Base de datos:** `bd_posventa`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/posventa` | Listar reportes |
| GET | `/api/posventa/{id}` | Obtener reporte por ID |
| POST | `/api/posventa` | Crear reporte |
| PUT | `/api/posventa/{id}` | Actualizar reporte |
| DELETE | `/api/posventa/{id}` | Eliminar reporte |

---

### 🛵 envios — Puerto 9088
Sistema de lista de envios

**Base de datos:** `bd_envios`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/envios` | Listar envios |
| GET | `/api/envios/{id}` | Obtener envio por ID |
| POST | `/api/envios` | Crear envio |
| PUT | `/api/envios/{id}` | Actualizar envios |
| DELETE | `/api/envios/{id}` | Eliminar envio |

---

## 🌐 API Gateway — Puerto 8080

Punto de entrada único para todos los microservicios.

| Ruta | Microservicio destino |
|---|---|
| `/auth/**` | auth:9080 |
| `/api/clientes/**` | clientes:9081 |
| `/api/compras/**` | compra:9082 |
| `/api/inventario/**` | inventario:9083 |
| `/api/pagos/**` | pagos:9084 |
| `/api/producto/**` | producto:9085 |
| `/api/recomendaciones/**` | recomendaciones:9086 |
| `/api/posventa/**` | posventa:9087 |
| `/api/envios/**` | envios:9088 |

---

## 📋 Swagger / OpenAPI

| Microservicio | URL |
|---|---|
| auth | http://localhost:9080/doc/swagger-ui.html |
| clientes | http://localhost:9081/doc/swagger-ui.html |
| compra | http://localhost:9082/doc/swagger-ui.html |
| inventario | http://localhost:9083/doc/swagger-ui.html |
| pagos | http://localhost:9084/doc/swagger-ui.html |
| producto | http://localhost:9085/doc/swagger-ui.html |
| recomendaciones | http://localhost:9086/doc/swagger-ui.html |
| posventa | http://localhost:9087/doc/swagger-ui.html |
| envios | http://localhost:9088/doc/swagger-ui.html |


---

## 🗃️ Bases de Datos

Cada microservicio tiene su propia base de datos MySQL.

| Microservicio | Base de datos |
|---|---|
| auth | bd_users |
| clientes | bd_cliente |
| compra | bd_compras |
| inventario | bd_inventario |
| pagos | bd_pagos |
| producto | bd_producto |
| recomendaciones | bd_recomendaciones |
| posventa | bd_posventa |
| envios | bd_envios |

---

## 🐳 Despliegue con Docker

### Requisitos
- Docker Desktop instalado
- Puerto 3306 disponible para MySQL

### Pasos

**1. Compilar todos los microservicios:**
```bat
build-all.bat
```

**2. Levantar los contenedores:**
```bash
docker-compose up --build
```

**3. Verificar que todo esté corriendo:**
```bash
docker ps
```

**4. Para detener:**
```bash
docker-compose down
```

> La base de datos MySQL se inicializa automáticamente con `mysql-init/init.sql`.

---

## 💻 Ejecución Local

### Requisitos
- Java 17
- Maven
- MySQL corriendo en `localhost:3306`

### Orden de arranque recomendado

1. MySQL
2. `auth`
3. `gateway`
4. `producto`
5. `clientes`
6. `inventario`
7. `pagos`
8. `compra`
9. `recomendaciones`
10. `posventa`

### Ejecutar un microservicio
```bash
cd <nombre-microservicio>
./mvnw spring-boot:run
```

---

## 📁 Estructura del Proyecto

```
CasoSemestral-DuocFullStack-I/
├── auth/
├── clientes/clientes/
├── compra/compra/
├── gateway/gateway/
├── inventario/inventario/
├── pagos/pagos/
├── producto/producto/
├── recomendaciones/
├── posventa/
├── mysql-init/
│   └── init.sql
├── docker-compose.yml
├── build-all.bat
├── iniciar-docker.bat
├── wachin.jpg
└── README.md
```