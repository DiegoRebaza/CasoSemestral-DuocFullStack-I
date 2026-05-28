-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tablas cliente y direcciones
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente  BIGINT          NOT NULL AUTO_INCREMENT,
    rut         VARCHAR(12)     NOT NULL,
    nombre      VARCHAR(150)    NOT NULL,
    correo      VARCHAR(150)    NOT NULL,
    PRIMARY KEY (id_cliente),
    CONSTRAINT uq_cliente_rut    UNIQUE (rut),
    CONSTRAINT uq_cliente_correo UNIQUE (correo)
);

-- changeset bravatta:2 labels:inicial comment:Creación de tabla direcciones con FK a cliente
CREATE TABLE IF NOT EXISTS direcciones (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    calle       VARCHAR(150)    NOT NULL,
    comuna      VARCHAR(40)     NOT NULL,
    cliente_id  BIGINT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_direcciones_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (id_cliente)
        ON DELETE CASCADE
);