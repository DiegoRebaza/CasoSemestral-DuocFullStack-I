--liquibase formatted sql

--changeset matias:1
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

--changeset matias:2
INSERT INTO users (email, password, role)
VALUES ('admin@bravatta.com', 'f865b53623b121fd34ee5426c792e5c33af8c227', 'ADMIN');