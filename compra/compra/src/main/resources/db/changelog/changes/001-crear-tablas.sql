--liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tabla compras
CREATE TABLE compras (
    id_compra BIGINT AUTO_INCREMENT NOT NULL,
    fecha_compra DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    id_cliente BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT PK_COMPRAS PRIMARY KEY (id_compra)
);

-- changeset bravatta:2 labels:inicial comment:Creación de tabla detalle_compra
CREATE TABLE detalle_compra (
    id_detalle_compra BIGINT AUTO_INCREMENT NOT NULL,
    compra_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    CONSTRAINT PK_DETALLE_COMPRA PRIMARY KEY (id_detalle_compra)
);

ALTER TABLE detalle_compra 
ADD CONSTRAINT fk_detalle_compra 
FOREIGN KEY (compra_id) 
REFERENCES compras (id_compra);