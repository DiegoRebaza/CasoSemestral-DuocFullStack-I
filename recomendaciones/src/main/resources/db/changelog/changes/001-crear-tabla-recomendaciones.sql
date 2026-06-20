--liquibase formatted sql

--changeset tu_nombre:001-crear-tabla-recomendaciones
CREATE TABLE IF NOT EXISTS recomendacion (
    id_recomendacion BIGINT NOT NULL AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    id_compra BIGINT NOT NULL,
    opinion_usuario VARCHAR(300) NOT NULL,
    puntuacion_afinidad DOUBLE NOT NULL,
    fecha_calculo DATETIME NOT NULL,
    PRIMARY KEY (id_recomendacion)
);