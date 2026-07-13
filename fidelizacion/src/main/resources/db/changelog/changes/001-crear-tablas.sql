-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tabla fidelizacion
CREATE TABLE IF NOT EXISTS fidelizacion (
    id_fidelizacion     BIGINT          NOT NULL AUTO_INCREMENT,
    id_cliente          BIGINT          NOT NULL,
    puntos_acumulados   INT             NOT NULL DEFAULT 0,
    nivel               VARCHAR(50)     NOT NULL DEFAULT 'PRINCIPIANTE',
    cupon_cumpleanos    BOOLEAN         NOT NULL DEFAULT FALSE,
    fecha_nacimiento    DATE,
    PRIMARY KEY (id_fidelizacion),
    CONSTRAINT uq_fidelizacion_cliente UNIQUE (id_cliente)
);

-- changeset bravatta:2 labels:inicial comment:Creación de tabla historial_puntos
CREATE TABLE IF NOT EXISTS historial_puntos (
    id_historial        BIGINT          NOT NULL AUTO_INCREMENT,
    id_fidelizacion     BIGINT          NOT NULL,
    id_pago             BIGINT          NOT NULL,
    puntos_sumados      INT             NOT NULL,
    fecha               DATETIME        NOT NULL,
    descripcion         VARCHAR(255),
    PRIMARY KEY (id_historial),
    CONSTRAINT fk_historial_fidelizacion
        FOREIGN KEY (id_fidelizacion)
        REFERENCES fidelizacion (id_fidelizacion)
        ON DELETE CASCADE
);