-- liquibase formatted sql

-- changeset bravatta:3 labels:seed comment:Insertar 10 registros en tabla cliente
INSERT INTO cliente (rut, nombre, correo) VALUES
('12.345.678-9', 'Juan Pérez González',      'juan.perez@gmail.com'),
('11.111.111-1', 'María López Soto',          'maria.lopez@gmail.com'),
('13.222.333-4', 'Carlos Ramírez Díaz',       'carlos.ramirez@hotmail.com'),
('14.444.555-6', 'Ana Martínez Flores',       'ana.martinez@gmail.com'),
('15.666.777-8', 'Pedro Sánchez Rojas',       'pedro.sanchez@outlook.com'),
('16.888.999-0', 'Valentina Torres Muñoz',    'valentina.torres@gmail.com'),
('17.123.456-7', 'Diego Herrera Castro',      'diego.herrera@hotmail.com'),
('18.234.567-8', 'Camila Fuentes Vega',       'camila.fuentes@gmail.com'),
('19.345.678-9', 'Sebastián Morales Pino',    'sebastian.morales@outlook.com'),
('20.456.789-0', 'Fernanda Castro Espinoza',  'fernanda.castro@gmail.com');

-- changeset bravatta:4 labels:seed comment:Insertar 10 registros en tabla direcciones
INSERT INTO direcciones (calle, comuna, cliente_id) VALUES
('Av. Providencia 1234', 'Providencia', 1),
('Los Leones 567', 'Vitacura', 2),
('Av. Libertador 890', 'Las Condes', 3),
('Calle Larga 321', 'Maipu',            4),
('Pasaje Los Aromos 45', 'La Florida', 5),
('Av. Grecia 678', 'Nunoa', 6),
('Calle Nueva 910', 'Santiago Centro', 7),
('Los Quillayes 234', 'La Reina', 8),
('Av. Vicuña Mackenna 1122', 'Macul', 9),
('Calle Blanco 333', 'Pudahuel', 10);