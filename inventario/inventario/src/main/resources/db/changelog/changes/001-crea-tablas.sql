-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creacion de tabla inventario
CREATE TABLE inventario (
    id_inventario BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL UNIQUE,
    stock_disponible INT NOT NULL,
    stock_minimo INT NOT NULL,
    ultima_actualizacion DATETIME
);