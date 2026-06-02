-- liquibase formatted sql

-- changeset bravatta:2 labels:seed comment:Insertar 10 registros en tabla transaccion
INSERT INTO transaccion (monto, metodo_pago, id_transaccion_externa) VALUES
(15000,  'WEBPAY',       'TXN-2024-000001'),
(32500,  'WEBPAY',       'TXN-2024-000002'),
(8900,   'TRANSFERENCIA','TXN-2024-000003'),
(47000,  'DEBITO',       'TXN-2024-000004'),
(12300,  'CREDITO',      'TXN-2024-000005'),
(5500,   'EFECTIVO',     'TXN-2024-000006'),
(99000,  'WEBPAY',       'TXN-2024-000007'),
(21000,  'TRANSFERENCIA','TXN-2024-000008'),
(63400,  'DEBITO',       'TXN-2024-000009'),
(18750,  'CREDITO',      'TXN-2024-000010');