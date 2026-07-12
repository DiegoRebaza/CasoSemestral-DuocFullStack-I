-- liquibase formatted sql

-- changeset bravatta:2 labels:seed comment:Insertar 10 registros en tabla transaccion
INSERT INTO transaccion (monto, metodo_pago, id_transaccion_externa) VALUES
(5200.00,  'WEBPAY',        'TXN-2026-000001'),
(11000.00, 'WEBPAY',        'TXN-2026-000002'),
(2500.00,  'TRANSFERENCIA', 'TXN-2026-000003'),
(7200.00,  'DEBITO',        'TXN-2026-000004'),
(16800.00, 'CREDITO',       'TXN-2026-000005'),
(12500.00, 'EFECTIVO',      'TXN-2026-000006'),
(4800.00,  'WEBPAY',        'TXN-2026-000007'),
(3000.00,  'TRANSFERENCIA', 'TXN-2026-000008'),
(6000.00,  'DEBITO',        'TXN-2026-000009'),
(13000.00, 'CREDITO',       'TXN-2026-000010');