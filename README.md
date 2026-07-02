# 🍦 Tienda de Helados Bravatta

Sistema de gestión y venta de helados desarrollado con arquitectura de microservicios usando Spring Boot.

---

## Integrantes

| Nombre | GitHub |
|---|---|
| Diego Rebaza | 
| Matias Pavez | — |

**Repositorio:** https://github.com/DiegoRebaza/CasoSemestral-DuocFullStack-I

---

## Arquitectura General

```
[Frontend - HTML/CSS/JS]  ←→          [BFF]
                                        ↓
                              [API Gateway :9090]
                                        ↓
        ┌──────────┬──────────┬─────────┼─────────┬──────────┬──────────┬──────────┬──────────┐
        ↓          ↓          ↓         ↓         ↓          ↓          ↓          ↓          ↓
     [auth]   [clientes]  [compra] [inventario] [pagos] [producto] [recomend.] [posventa]  [gateway]
     :9096     :9093       :9091     :9092       :9094    :9095      :9098       :9099
        ↓          ↓          ↓         ↓         ↓          ↓          ↓          ↓
   [bd_users] [bd_cliente] [bd_compras][bd_inv] [bd_pagos][bd_prod] [bd_recom.] [bd_posv.]
```

### Comunicación entre microservicios

| Origen | Destino(s) | Motivo |
|---|---|---|
| `compra` | `clientes`, `producto` | Valida cliente y producto antes de registrar una compra |
| `inventario` | `producto` | Valida que el producto exista antes de registrar stock |
| `recomendaciones` | `gateway` | Comunicación vía gateway |
| `posventa` | `gateway` | Comunicación vía gateway |

---

## 📦 Microservicios

### 🔐 auth — Puerto 9096
Gestión de autenticación de usuarios. Registro, login y generación de tokens JWT.

**Base de datos:** `bd_users`

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión, retorna JWT |
| POST | `/api/auth/register` | Registrar nuevo usuario |

---

### 👤 clientes — Puerto 9093
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

### 🛒 compra — Puerto 9091
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

### 📦 inventario — Puerto 9092
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

### 💳 pagos — Puerto 9094
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

### 🍦 producto — Puerto 9095
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

### ⭐ recomendaciones — Puerto 9098
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

### 🛠️ posventa — Puerto 9099
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

## 🌐 API Gateway — Puerto 9090

Punto de entrada único para todos los microservicios.

| Ruta | Microservicio destino |
|---|---|
| `/api/auth/**` | auth:9096 |
| `/api/clientes/**` | clientes:9093 |
| `/api/compras/**` | compra:9091 |
| `/api/inventario/**` | inventario:9092 |
| `/api/pagos/**` | pagos:9094 |
| `/api/producto/**` | producto:9095 |
| `/api/recomendaciones/**` | recomendaciones:9098 |
| `/api/posventa/**` | posventa:9099 |

---

## 📋 Swagger / OpenAPI

| Microservicio | URL |
|---|---|
| auth | http://localhost:9096/doc/swagger-ui.html |
| clientes | http://localhost:9093/doc/swagger-ui.html |
| compra | http://localhost:9091/doc/swagger-ui.html |
| inventario | http://localhost:9092/doc/swagger-ui.html |
| pagos | http://localhost:9094/doc/swagger-ui.html |
| producto | http://localhost:9095/doc/swagger-ui.html |
| recomendaciones | http://localhost:9098/doc/swagger-ui.html |
| posventa | http://localhost:9099/doc/swagger-ui.html |

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

---

## ⚙️ Variables de Entorno

| Variable | Descripción | Valor ejemplo |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `docker` |
| `JWT_SECRET` | Clave secreta JWT | `8d5hg01g8yt91p6t...` |

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
└── README.md
```

---

## 📎 Anexo — Configuraciones por microservicio

### auth (`application.properties`)
```properties
spring.application.name=auth
server.port=9096
spring.datasource.url=jdbc:mysql://localhost:3306/bd_users?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
jwt.secret=8d5hg01g8yt91p6tczc5auxkao4u8og9m9e02x7lv64q4ref97ahh2il5sgsfck3h4h94sj9wlardkeh
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### auth (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_users?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
```

### clientes (`application.properties`)
```properties
spring.application.name=clientes
server.port=9093
spring.datasource.url=jdbc:mysql://localhost:3306/bd_cliente?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### clientes (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_cliente?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
```

### compra (`application.properties`)
```properties
spring.application.name=compra
server.port=9091
spring.datasource.url=jdbc:mysql://localhost:3306/bd_compras?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://localhost:9090
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### compra (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_compras?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://gateway:9090
```

### inventario (`application.properties`)
```properties
spring.application.name=inventario
server.port=9092
spring.datasource.url=jdbc:mysql://localhost:3306/bd_inventario?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
producto.service.url=http://localhost:9090
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### inventario (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_inventario?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
producto.service.url=http://producto:9095
```

### pagos (`application.properties`)
```properties
spring.application.name=pagos
server.port=9094
spring.datasource.url=jdbc:mysql://localhost:3306/bd_pagos?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### pagos (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_pagos?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
```

### producto (`application.properties`)
```properties
spring.application.name=producto
server.port=9095
spring.datasource.url=jdbc:mysql://localhost:3306/bd_producto?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### producto (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_producto?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
```

### recomendaciones (`application.properties`)
```properties
spring.application.name=recomendaciones
server.port=9098
spring.datasource.url=jdbc:mysql://localhost:3306/bd_recomendaciones?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://localhost:9090
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### recomendaciones (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_recomendaciones?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://gateway:9090
```

### posventa (`application.properties`)
```properties
spring.application.name=posventa
server.port=9099
spring.datasource.url=jdbc:mysql://localhost:3306/bd_posventa?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://localhost:9090
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

### posventa (`application-docker.properties`)
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/bd_posventa?createDatabaseIfNotExist=true&serverTimezone=America/Santiago
gateway.url=http://gateway:9090
```

### gateway (`application.yml`)
```yaml
server:
  port: 9090
spring:
  application:
    name: gateway
  main:
    web-application-type: reactive
  cloud:
    gateway:
      routes:
        - id: auth
          uri: http://localhost:9096
          predicates:
            - Path=/api/auth/**
        - id: clientes
          uri: http://localhost:9093
          predicates:
            - Path=/api/clientes/**
        - id: compra
          uri: http://localhost:9091
          predicates:
            - Path=/api/compras/**
        - id: inventario
          uri: http://localhost:9092
          predicates:
            - Path=/api/inventario/**
        - id: pagos
          uri: http://localhost:9094
          predicates:
            - Path=/api/pagos/**
        - id: producto
          uri: http://localhost:9095
          predicates:
            - Path=/api/producto/**
        - id: recomendaciones
          uri: http://localhost:9098
          predicates:
            - Path=/api/recomendaciones/**
        - id: posventa
          uri: http://localhost:9099
          predicates:
            - Path=/api/posventa/**
```

### gateway (`application-docker.yml`)
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth
          uri: http://auth:9096
          predicates:
            - Path=/api/auth/**
        - id: clientes
          uri: http://clientes:9093
          predicates:
            - Path=/api/clientes/**
        - id: compra
          uri: http://compra:9091
          predicates:
            - Path=/api/compras/**
        - id: inventario
          uri: http://inventario:9092
          predicates:
            - Path=/api/inventario/**
        - id: pagos
          uri: http://pagos:9094
          predicates:
            - Path=/api/pagos/**
        - id: producto
          uri: http://producto:9095
          predicates:
            - Path=/api/producto/**
        - id: recomendaciones
          uri: http://recomendaciones:9098
          predicates:
            - Path=/api/recomendaciones/**
        - id: posventa
          uri: http://posventa:9099
          predicates:
            - Path=/api/posventa/**
```
