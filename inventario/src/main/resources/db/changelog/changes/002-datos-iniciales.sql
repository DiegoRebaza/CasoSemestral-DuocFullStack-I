-- liquibase formatted sql

-- changeset bravatta:2 labels:inicial comment:Insercion de datos iniciales en inventario
INSERT INTO inventario (producto_id, stock_disponible, stock_minimo) VALUES
(1, 100, 10),
(2, 50, 5),
(3, 200, 20),
(4, 75, 8),
(5, 30, 5),
(6, 120, 15),
(7, 45, 5),
(8, 60, 10),
(9, 90, 10),
(10, 25, 5);