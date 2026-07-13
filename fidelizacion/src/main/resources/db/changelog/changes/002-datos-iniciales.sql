-- liquibase formatted sql

-- changeset bravatta:3 labels:seed comment:Insertar datos de prueba en fidelizacion
INSERT INTO fidelizacion (id_cliente, puntos_acumulados, nivel, cupon_cumpleanos, fecha_nacimiento) VALUES
(1,  50,  'PRINCIPIANTE',      FALSE, '1995-07-12'),
(2,  120, 'AMANTE_DEL_HELADO', FALSE, '1990-03-22'),
(3,  310, 'MAESTRO_HELADERO',  FALSE, '1988-11-05'),
(4,  0,   'PRINCIPIANTE',      FALSE, '2000-01-15'),
(5,  200, 'AMANTE_DEL_HELADO', TRUE,  '1997-06-30');

-- changeset bravatta:4 labels:seed comment:Insertar historial de puntos de prueba
INSERT INTO historial_puntos (id_fidelizacion, id_pago, puntos_sumados, fecha, descripcion) VALUES
(1, 1, 10, NOW(), 'Puntos por pago ID: 1'),
(1, 2, 10, NOW(), 'Puntos por pago ID: 2'),
(2, 3, 10, NOW(), 'Puntos por pago ID: 3'),
(3, 4, 10, NOW(), 'Puntos por pago ID: 4'),
(5, 5, 10, NOW(), 'Puntos por pago ID: 5');