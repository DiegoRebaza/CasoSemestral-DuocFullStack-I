--liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tabla compras
CREATE TABLE compras (
    id BIGINT AUTO_INCREMENT NOT NULL,
    fecha_compra DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT PK_COMPRAS PRIMARY KEY (id)
);

-- changeset bravatta:2 labels:inicial comment:Creación de tabla detalle_compras
CREATE TABLE detalle_compras (
    id BIGINT AUTO_INCREMENT NOT NULL,
    compra_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    CONSTRAINT PK_DETALLE_COMPRAS PRIMARY KEY (id)
);

ALTER TABLE detalle_compras 
ADD CONSTRAINT fk_detalle_compra 
FOREIGN KEY (compra_id) 
REFERENCES compras (id);