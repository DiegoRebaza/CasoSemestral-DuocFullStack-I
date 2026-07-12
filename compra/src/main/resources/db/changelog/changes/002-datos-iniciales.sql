--liquibase formatted sql

-- changeset bravatta:3 labels:seed comment:Insertar 10 registros en tabla compras
INSERT INTO compras (fecha_compra, total, id_cliente, estado) VALUES 
('2026-06-01 10:30:00', 5200.00, 1, 'COMPLETADO'),
('2026-06-01 11:15:00', 11000.00, 2, 'COMPLETADO'),
('2026-06-01 14:22:00', 2500.00, 3, 'PENDIENTE'),
('2026-06-02 09:05:00', 7200.00, 1, 'COMPLETADO'),
('2026-06-02 10:40:00', 16800.00, 4, 'PROCESANDO'),
('2026-06-02 12:10:00', 12500.00, 5, 'COMPLETADO'),
('2026-06-02 13:55:00', 4800.00, 2, 'CANCELADO'),
('2026-06-02 15:18:00', 3000.00, 6, 'COMPLETADO'),
('2026-06-02 16:45:00', 6000.00, 3, 'PENDIENTE'),
('2026-06-02 17:30:00', 13000.00, 7, 'COMPLETADO');

-- changeset bravatta:4 labels:seed comment:Insertar registros en tabla detalle_compra
INSERT INTO detalle_compra (compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES 
(1, 1, 2, 1500.00, 3000.00),
(1, 2, 1, 2200.00, 2200.00),
(2, 5, 2, 5500.00, 11000.00),
(3, 3, 1, 2500.00, 2500.00),
(4, 8, 1, 7200.00, 7200.00),
(5, 9, 1, 10500.00, 10500.00),
(5, 7, 1, 6300.00, 6300.00),
(6, 3, 5, 2500.00, 12500.00),
(7, 10, 1, 4800.00, 4800.00),
(9, 3, 5, 2500.00, 12500.00),
(10, 10, 1, 4800.00, 4800.00),
(8, 1, 2, 1500.00, 3000.00);