Script de la base de datos
CREATE DATABASE tiendapcdos;
USE tiendapcdos;

CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombreCliente VARCHAR(100),
    emailCliente VARCHAR(100),
    telefonoCliente int
);
CREATE TABLE productos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombreProducto VARCHAR(100),
    precioProducto DECIMAL(10,2),
    stockProducto INT
);
select * from clientes;
select * from productos;
INSERT INTO clientes (nombreCliente, emailCliente, telefonoCliente) 
VALUES ('Juan Perez', 'juan@email.com', 123456789);
