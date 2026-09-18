DROP TABLE IF EXISTS empleados;

CREATE TABLE empleados (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('admin', 'usuario')),
    cargo VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
