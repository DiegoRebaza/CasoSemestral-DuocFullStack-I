--liquibase formatted sql

-- changeset bravatta:3 labels:seed comment:Insertar 10 registros en tabla compras
INSERT INTO compras (fecha_compra, total, id_cliente, estado) VALUES 
('2026-06-01 10:30:00', 45.50, 1, 'COMPLETADO'),
('2026-06-01 11:15:00', 120.00, 2, 'COMPLETADO'),
('2026-06-01 14:22:00', 15.75, 3, 'PENDIENTE'),
('2026-06-02 09:05:00', 89.99, 1, 'COMPLETADO'),
('2026-06-02 10:40:00', 210.50, 4, 'PROCESANDO'),
('2026-06-02 12:10:00', 35.00, 5, 'COMPLETADO'),
('2026-06-02 13:55:00', 12.25, 2, 'CANCELADO'),
('2026-06-02 15:18:00', 78.40, 6, 'COMPLETADO'),
('2026-06-02 16:45:00', 105.00, 3, 'PENDIENTE'),
('2026-06-02 17:30:00', 64.20, 7, 'COMPLETADO');

-- changeset bravatta:4 labels:seed comment:Insertar 10 registros en tabla detalle_compras
INSERT INTO detalle_compras (compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES 
(1, 101, 2, 15.00, 30.00),
(1, 102, 1, 15.50, 15.50),
(2, 105, 1, 120.00, 120.00),
(3, 101, 1, 15.75, 15.75),
(4, 108, 1, 89.99, 89.99),
(5, 110, 2, 100.00, 200.00),
(5, 102, 1, 10.50, 10.50),
(6, 115, 5, 7.00, 35.00),
(7, 120, 1, 12.25, 12.25),
(8, 101, 2, 39.20, 78.40);