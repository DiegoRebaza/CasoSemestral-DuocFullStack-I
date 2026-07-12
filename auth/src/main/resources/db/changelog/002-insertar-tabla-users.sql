--liquibase formatted sql

-- changeset bravatta:2 comment:Insertar usuario admin

INSERT INTO users (email, password, role) VALUES 
('admin@bravatta.com', 'f865b53623b121fd34ee5426c792e5c33af8c227', 'ADMIN');