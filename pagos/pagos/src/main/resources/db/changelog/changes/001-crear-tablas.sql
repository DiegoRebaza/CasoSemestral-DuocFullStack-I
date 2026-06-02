-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tabla transaccion
CREATE TABLE IF NOT EXISTS transaccion (
    id_transaccion          BIGINT       NOT NULL AUTO_INCREMENT,
    monto                   INTEGER      NOT NULL,
    metodo_pago             VARCHAR(30)  NOT NULL,
    id_transaccion_externa  VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_transaccion),
    CONSTRAINT uq_transaccion_externa UNIQUE (id_transaccion_externa)
);
