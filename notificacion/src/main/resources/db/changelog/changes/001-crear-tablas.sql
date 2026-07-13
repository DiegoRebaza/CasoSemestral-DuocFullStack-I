-- liquibase formatted sql

-- changeset bravatta:1 labels:inicial comment:Creación de tabla notificacion
CREATE TABLE IF NOT EXISTS notificacion (
    id_notificacion  BIGINT          NOT NULL AUTO_INCREMENT,
    id_cliente       BIGINT          NOT NULL,
    tipo             VARCHAR(20)     NOT NULL DEFAULT 'SMS',
    evento           VARCHAR(50)     NOT NULL,
    mensaje          VARCHAR(500)    NOT NULL,
    estado           VARCHAR(20)     NOT NULL DEFAULT 'ENVIADO',
    fecha_envio      DATETIME        NOT NULL,
    PRIMARY KEY (id_notificacion)
);