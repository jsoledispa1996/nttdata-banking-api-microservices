-- Script de instalación para la base de datos de clientes (customerdb)

CREATE DATABASE IF NOT EXISTS customerdb;
USE customerdb;
GRANT ALL PRIVILEGES ON customerdb.* TO 'banking'@'%';
FLUSH PRIVILEGES;

CREATE TABLE ba_personas (
    pe_id_persona INT AUTO_INCREMENT PRIMARY KEY,
    pe_nombre VARCHAR(100) NOT NULL,
    pe_genero VARCHAR(20) NOT NULL,
    pe_edad INT NOT NULL,
    pe_identificacion VARCHAR(20) NOT NULL,
    pe_direccion VARCHAR(255) NOT NULL,
    pe_telefono VARCHAR(20) NOT NULL,
    pe_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pe_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE ba_clientes (
    cl_id_persona INT PRIMARY KEY,
    cl_id_cliente VARCHAR(20) UNIQUE NOT NULL,
    cl_contrasena VARCHAR(255) NOT NULL,
    cl_estado BOOLEAN NOT NULL,
    cl_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cl_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cl_id_persona) REFERENCES ba_personas(pe_id_persona)
);

-- ========================================
-- Datos de prueba: Clientes con información completa
-- ========================================

-- Cliente 1: Jose Andres Soledispa Yagual 
INSERT INTO ba_personas (pe_nombre, pe_genero, pe_edad, pe_identificacion, pe_direccion, pe_telefono) 
VALUES ('Jose Andres Soledispa Yagual', 'Masculino', 32, '1234567891', 'Av. Principal 123, Guayaquil', '0991234567');

INSERT INTO ba_clientes (cl_id_persona, cl_id_cliente, cl_contrasena, cl_estado) 
VALUES (1, 'CLI001', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE); -- password: 1234

-- Cliente 2: Erick Geovanny Soledispa Yagual
INSERT INTO ba_personas (pe_nombre, pe_genero, pe_edad, pe_identificacion, pe_direccion, pe_telefono) 
VALUES ('Erick Geovanny Soledispa Yagual', 'Masculino', 28, '1234567892', 'Calle Las Flores 456, Guayaquil', '0991234568');

INSERT INTO ba_clientes (cl_id_persona, cl_id_cliente, cl_contrasena, cl_estado) 
VALUES (2, 'CLI002', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE); -- password: 1234

-- Cliente 3: Pedro Cornelio Guale Gonzalez
INSERT INTO ba_personas (pe_nombre, pe_genero, pe_edad, pe_identificacion, pe_direccion, pe_telefono) 
VALUES ('Pedro Cornelio Guale Gonzalez', 'Masculino', 45, '1234567893', 'Av. Central 789, Salinas', '0991234569');

INSERT INTO ba_clientes (cl_id_persona, cl_id_cliente, cl_contrasena, cl_estado) 
VALUES (3, 'CLI003', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE); -- password: 1234

-- Cliente 4: Judith Juliana Alfonzo Morales
INSERT INTO ba_personas (pe_nombre, pe_genero, pe_edad, pe_identificacion, pe_direccion, pe_telefono) 
VALUES ('Judith Juliana Alfonzo Morales', 'Femenino', 35, '1234567894', 'Urbanización Los Pinos 321, Quito', '0991234570');

INSERT INTO ba_clientes (cl_id_persona, cl_id_cliente, cl_contrasena, cl_estado) 
VALUES (4, 'CLI004', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE); -- password: 1234

-- Cliente 5: Carmen Joanca Guale Morales
INSERT INTO ba_personas (pe_nombre, pe_genero, pe_edad, pe_identificacion, pe_direccion, pe_telefono) 
VALUES ('Carmen Joanca Guale Morales', 'Femenino', 29, '1234567895', 'Calle Libertad 654, Cuenca', '0991234571');

INSERT INTO ba_clientes (cl_id_persona, cl_id_cliente, cl_contrasena, cl_estado) 
VALUES (5, 'CLI005', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE); -- password: 1234

-- Fin del script de instalación de customerdb.
