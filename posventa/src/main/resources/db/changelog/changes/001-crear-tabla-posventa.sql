-- liquibase formatted sql
 
-- changeset bravatta:1 labels:inicial comment:Creación de tabla posventa
CREATE TABLE IF NOT EXISTS posventa (
    id_posventa  BIGINT          NOT NULL AUTO_INCREMENT,
    id_cliente   BIGINT          NOT NULL,
    id_compra    BIGINT          NOT NULL,
    motivo       VARCHAR(500)    NOT NULL,
    estado       VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha        DATETIME        NOT NULL,
    PRIMARY KEY (id_posventa)
);
