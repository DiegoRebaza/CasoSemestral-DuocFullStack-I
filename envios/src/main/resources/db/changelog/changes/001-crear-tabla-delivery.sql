
CREATE TABLE IF NOT EXISTS envios (
    id_envio BIGINT NOT NULL AUTO_INCREMENT,
    id_compra BIGINT NOT NULL,
    direccion_entrega VARCHAR(255) NOT NULL,
    estado_envio VARCHAR(20) NOT NULL,
    repartidor_asignado VARCHAR(150),
    fecha_estimada_entrega DATE,
    fecha_registro DATETIME NOT NULL,
    PRIMARY KEY (id_envio)
);