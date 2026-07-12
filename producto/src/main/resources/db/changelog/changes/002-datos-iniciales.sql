-- liquibase formatted sql

-- changeset bravatta:2 labels:seed comment:Insertar 10 registros en tabla producto
INSERT INTO producto (nombre, precio_base, sabor) VALUES
('Paleta de Agua Simple', 1500, 'Frambuesa'),
('Paleta de Crema Bañada', 2200, 'Vainilla Crocante'),
('Cono Simple Barquillo', 2500, 'Chocolate Suizo'),
('Cono Doble Barquillo', 3800, 'Frutilla y Chirimoya'),
('Pote 500ml Artesanal', 5500, 'Manjar Nuez'),
('Pote 500ml Artesanal Premium', 6000, 'Pistacho'),
('Casata 1 Litro Clásica', 6500, 'Tres Leches'),
('Casata 1 Litro Selección', 7200, 'Menta Chips'),
('Casata Familiar 2.5 Litros', 10500, 'Piña'),
('Pack 6 Mini Paletas', 4800, 'Chocolate Blanco');