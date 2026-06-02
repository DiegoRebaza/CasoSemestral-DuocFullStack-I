-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creacion de tabla producto
CREATE TABLE IF NOT EXISTS producto (
    id_producto   BIGINT       NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(100) NOT NULL,
    precio_base   INTEGER      NOT NULL,
    sabor         VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id_producto)
);