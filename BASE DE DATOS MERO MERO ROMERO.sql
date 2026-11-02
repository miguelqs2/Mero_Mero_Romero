CREATE DATABASE mero_mero_romero DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mero_mero_romero;

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE productos_precios (
    id_precio INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tamano VARCHAR(50) NOT NULL DEFAULT 'Unico',
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 50,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',
    tiempo_preparacion VARCHAR(50) DEFAULT '20 min',
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pedidos (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL,
    nombre_mesera VARCHAR(50),
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_hora_pedido DATETIME,
    fecha_hora_realizado DATETIME,
    metodo_pago VARCHAR(50) DEFAULT 'Efectivo',
    observaciones VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE items_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    id_precio INT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_precio) REFERENCES productos_precios(id_precio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE meseras (
    id_mesera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    activa BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO categorias (id_categoria, nombre) VALUES
(1, 'Ceviche'),
(2, 'Leche de Tigre'),
(3, 'Jaleas'),
(4, 'Arroz'),
(5, 'Levanta Muertos'),
(6, 'Platos Especiales'),
(7, 'Pescado Frito'),
(8, 'Trios Marinos'),
(9, 'Duos Marinos'),
(10, 'Bebidas'),
(11, 'Platos Criollos'),
(12, 'Entradas');


INSERT INTO meseras (nombre) VALUES 
('Maria'), ('Ana'), ('Sofia'), ('Carlos'), ('Jenny'), ('Nicole');

INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(1, 1, 'Ceviche de Pescado'),
(2, 1, 'Ceviche de Pescado c/ Chicharron de Pota'),
(3, 1, 'Ceviche Mixto'),
(4, 1, 'Ceviche de Conchas Negras'),
(5, 1, 'Ceviche de Pota'),
(6, 1, 'Ceviche de Langostinos'),
(7, 1, 'Ceviche de Mariscos'),
(8, 1, 'Ceviche Clasico (Conchas Negras + Ceviche de Pescado)'),
(9, 1, 'Choritos a la Chalaca');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(1, 'Personal', 25.00, 30, '15 min'), (1, 'Mediano', 70.00, 15, '20 min'), (1, 'Familiar', 100.00, 10, '25 min'),
(2, 'Personal', 30.00, 20, '18 min'), (2, 'Mediano', 90.00, 10, '22 min'), (2, 'Familiar', 120.00, 8, '28 min'),
(3, 'Personal', 35.00, 25, '18 min'), (3, 'Mediano', 80.00, 12, '22 min'), (3, 'Familiar', 120.00, 8, '28 min'),
(4, 'Personal', 35.00, 15, '15 min'),
(5, 'Personal', 25.00, 20, '15 min'),
(6, 'Personal', 45.00, 10, '20 min'),
(7, 'Personal', 40.00, 15, '18 min'),
(8, 'Personal', 45.00, 10, '20 min'),
(9, 'Personal', 35.00, 12, '10 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(10, 2, 'Leche de Tigre de Pescado'),
(11, 2, 'Leche de Tigre Mixto'),
(12, 2, 'Leche de Pantera'),
(13, 2, 'Leche de Monja');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(10, 'Personal', 18.00, 20, '10 min'),
(11, 'Personal', 20.00, 20, '10 min'),
(12, 'Personal', 25.00, 15, '10 min'),
(13, 'Personal', 22.00, 15, '10 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(14, 3, 'Jalea Mixtas'),
(15, 3, 'Jalea de Pescado'),
(16, 3, 'Chicharron de Pescado'),
(17, 3, 'Chicharron Mixto'),
(18, 3, 'Chicharron de Pota'),
(19, 3, 'Chicharron de Langostinos'),
(20, 3, 'Chicharron de Calamar'),
(21, 3, 'Chicharron de Mariscos');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(14, 'Personal', 40.00, 15, '25 min'), (14, 'Mediano', 90.00, 8, '30 min'), (14, 'Familiar', 120.00, 5, '35 min'),
(15, 'Personal', 45.00, 12, '25 min'), (15, 'Mediano', 90.00, 8, '30 min'), (15, 'Familiar', 120.00, 5, '35 min'),
(16, 'Personal', 45.00, 12, '25 min'), (16, 'Mediano', 90.00, 8, '30 min'), (16, 'Familiar', 120.00, 5, '35 min'),
(17, 'Personal', 40.00, 15, '25 min'), (17, 'Mediano', 90.00, 8, '30 min'), (17, 'Familiar', 120.00, 5, '35 min'),
(18, 'Personal', 30.00, 20, '20 min'),
(19, 'Personal', 45.00, 10, '25 min'),
(20, 'Personal', 45.00, 10, '25 min'),
(21, 'Personal', 45.00, 10, '25 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(22, 4, 'Arroz c/ Mariscos'),
(23, 4, 'Arroz Chaufa de Mariscos'),
(24, 4, 'Arroz Chaufa de Pescado'),
(25, 4, 'Arroz Chaufa de Langostinos'),
(26, 4, 'Arroz c/ Conchas Negras');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(22, 'Personal', 35.00, 15, '20 min'), (22, 'Mediano', 80.00, 8, '25 min'), (22, 'Familiar', 120.00, 5, '30 min'),
(23, 'Personal', 35.00, 15, '20 min'), (23, 'Mediano', 80.00, 8, '25 min'), (23, 'Familiar', 120.00, 5, '30 min'),
(24, 'Personal', 40.00, 12, '20 min'),
(25, 'Personal', 45.00, 10, '22 min'),
(26, 'Personal', 50.00, 8, '25 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(27, 5, 'Parihuela de Filete de Pescado'),
(28, 5, 'Parihuela de Cabrilla'),
(29, 5, 'Parihuela de Tranboyo'),
(30, 5, 'Sudado de Filete de Pescado'),
(31, 5, 'Sudado de Cabrilla'),
(32, 5, 'Sudado de Tramboyo'),
(33, 5, 'Chupe de Pescado'),
(34, 5, 'Chupe de Camarones'),
(35, 5, 'Chupe de Langostinos'),
(36, 5, 'Sopa de Choros'),
(37, 5, 'Sopa Salvaje'),
(38, 5, 'Chilcano Especial');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(27, 'Personal', 45.00, 10, '25 min'),
(28, 'Personal', 50.00, 8, '25 min'),
(29, 'Personal', 50.00, 8, '25 min'),
(30, 'Personal', 40.00, 12, '20 min'),
(31, 'Personal', 45.00, 10, '22 min'),
(32, 'Personal', 45.00, 10, '22 min'),
(33, 'Personal', 40.00, 12, '20 min'),
(34, 'Personal', 40.00, 10, '20 min'),
(35, 'Personal', 40.00, 10, '20 min'),
(36, 'Personal', 40.00, 10, '20 min'),
(37, 'Personal', 30.00, 15, '15 min'),
(38, 'Personal', 25.00, 15, '12 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(39, 6, 'Tacu Tacu en Salsa de Langostinos'),
(40, 6, 'Tacu Tacu en Salsa de Mariscos'),
(41, 6, 'Pescado a lo Macho'),
(42, 6, 'Pescado a la Chorrillana');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(39, 'Personal', 45.00, 10, '25 min'),
(40, 'Personal', 45.00, 10, '25 min'),
(41, 'Personal', 40.00, 12, '22 min'),
(42, 'Personal', 40.00, 12, '22 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(43, 7, 'Cabrilla Frita'),
(44, 7, 'Cachema Frita'),
(45, 7, 'Doncella Frita'),
(46, 7, 'Trucha Frita'),
(47, 7, 'Filete de Pescado'),
(48, 7, 'Pescado a la Plancha');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(43, 'Personal', 35.00, 15, '20 min'),
(44, 'Personal', 35.00, 15, '20 min'),
(45, 'Personal', 30.00, 15, '20 min'),
(46, 'Personal', 25.00, 15, '18 min'),
(47, 'Personal', 30.00, 15, '18 min'),
(48, 'Personal', 30.00, 15, '18 min');


INSERT INTO productos (id_producto, id_categoria, nombre, descripcion) VALUES
(49, 8, 'Trio Marino 1 : Arroz c/ Mariscos + Chicharrón de Pota + Ceviche de Pescado', 'Arroz c/ Mariscos + Chicharrón de Pota + Ceviche de Pescado'),
(50, 8, 'Trio Marino 2 : Arroz Chaufa c/ Mariscos + Chicharrón Mixto + Ceviche de Pescado', 'Arroz Chaufa c/ Mariscos + Chicharrón Mixto + Ceviche de Pescado');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(49, 'Personal', 40.00, 12, '25 min'), (49, 'Mediano', 90.00, 8, '30 min'), (49, 'Familiar', 130.00, 5, '35 min'),
(50, 'Personal', 45.00, 12, '25 min'), (50, 'Mediano', 90.00, 8, '30 min'), (50, 'Familiar', 130.00, 5, '35 min');

INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(51, 9, 'Dúo Marino 1 : Arroz c/ Mariscos + Ceviche de Pescado'),
(52, 9, 'Dúo Marino 2 : Arroz Chaufa de Mariscos + Ceviche de Pescado'),
(53, 9, 'Dúo Marino 3 : Arroz Chaufa de Pescado + Ceviche de Pescado'),
(54, 9, 'Dúo Marino 4 : Chicharrón de Pescado + Ceviche de Pescado'),
(55, 9, 'Dúo Marino 5 : Jalea Mixta + Ceviche de Pescado');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(51, 'Personal', 40.00, 12, '25 min'), (51, 'Mediano', 90.00, 8, '30 min'), (51, 'Familiar', 130.00, 5, '35 min'),
(52, 'Personal', 40.00, 12, '25 min'), (52, 'Mediano', 90.00, 8, '30 min'), (52, 'Familiar', 130.00, 5, '35 min'),
(53, 'Personal', 40.00, 12, '25 min'), (53, 'Mediano', 90.00, 8, '30 min'), (53, 'Familiar', 130.00, 5, '35 min'),
(54, 'Personal', 40.00, 12, '25 min'), (54, 'Mediano', 90.00, 8, '30 min'), (54, 'Familiar', 130.00, 5, '35 min'),
(55, 'Personal', 40.00, 12, '25 min'), (55, 'Mediano', 90.00, 8, '30 min'), (55, 'Familiar', 130.00, 5, '35 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(56, 10, 'Chicha Morada'),
(57, 10, 'Chicha de Jora'),
(58, 10, 'Agua de Manzana'),
(59, 10, 'Maracuya'),
(60, 10, 'Gaseosa Personal'),
(61, 10, 'Gaseosa Gordita'),
(62, 10, 'Gaseosa de 1 Lt'),
(63, 10, 'Gaseosa de 1 1/2 Lt'),
(64, 10, 'Agua Mineral'),
(65, 10, 'Cerveza Pilsen'),
(66, 10, 'Cusqueña Negra'),
(67, 10, 'Cusqueña de Trigo');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(56, 'Litro', 12.00, 30, '3 min'), (56, '1/2 Litro', 6.00, 30, '3 min'), (56, 'Vaso', 3.00, 50, '2 min'),
(57, 'Litro', 15.00, 20, '3 min'), (57, '1/2 Litro', 8.00, 20, '3 min'), (57, 'Vaso', 4.00, 30, '2 min'),
(58, 'Litro', 12.00, 30, '3 min'), (58, '1/2 Litro', 6.00, 30, '3 min'), (58, 'Vaso', 3.00, 50, '2 min'),
(59, 'Litro', 12.00, 30, '3 min'), (59, '1/2 Litro', 6.00, 30, '3 min'), (59, 'Vaso', 3.00, 50, '2 min'),
(60, 'Personal', 3.00, 50, '1 min'),
(61, 'Personal', 5.00, 40, '1 min'),
(62, 'Personal', 8.00, 30, '1 min'),
(63, 'Personal', 10.00, 30, '1 min'),
(64, 'Personal', 3.00, 50, '1 min'),
(65, 'Personal', 12.00, 40, '1 min'),
(66, 'Personal', 13.00, 30, '1 min'),
(67, 'Personal', 13.00, 30, '1 min');


INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(68, 11, 'Lomo Saltado'),
(69, 11, 'Lomo Saltado a lo Pobre'),
(70, 11, 'Saltado de Pollo'),
(71, 11, 'Saltado de Pollo a lo Pobre'),
(72, 11, 'Arroz Chaufa de Pollo'),
(73, 11, 'Arroz Chaufa de Carne'),
(74, 11, 'Pollo a la Plancha c/ Papas Fritas'),
(75, 11, 'Bisteck c/ Papas Fritas'),
(76, 11, 'Bisteck a lo Pobre'),
(77, 11, 'Bisteck Apanado c/ Papas Fritas'),
(78, 11, 'Pollo Broaster 1/4'),
(79, 11, 'Pollada Frita 1/4'),
(80, 11, 'Pollo Frito 1/4'),
(81, 11, 'Churrasco c/ Papas Fritas'),
(82, 11, 'Churrasco a lo Pobre'),
(83, 11, 'Milanesa c/ Papas Fritas'),
(84, 11, 'Suprema de Pollo c/ Papas Fritas'),
(85, 11, 'Chicharron de Pollo c/ Papas Fritas'),
(86, 11, 'Tallarin Saltado de Carne'),
(87, 11, 'Tallarin Saltado de Pollo'),
(88, 11, 'Tallarin a la Huancaina c/ Bisteck Apanado'),
(89, 11, 'Tallarin Verde con Bisteck Apanado'),
(90, 11, 'Chuleta c/ Papas Fritas'),
(91, 11, 'Tacu Tacu c/ Lomo Saltado'),
(92, 11, 'Tacu Tacu c/ Churrasco'),
(93, 11, 'Tacu Tacu c/ Bisteck Apanado');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(68, 'Personal', 30.00, 15, '20 min'),
(69, 'Personal', 35.00, 12, '22 min'),
(70, 'Personal', 28.00, 15, '18 min'),
(71, 'Personal', 33.00, 12, '20 min'),
(72, 'Personal', 25.00, 15, '18 min'),
(73, 'Personal', 28.00, 15, '18 min'),
(74, 'Personal', 28.00, 15, '18 min'),
(75, 'Personal', 30.00, 15, '20 min'),
(76, 'Personal', 35.00, 12, '22 min'),
(77, 'Personal', 30.00, 15, '20 min'),
(78, 'Personal', 30.00, 15, '20 min'),
(79, 'Personal', 28.00, 15, '20 min'),
(80, 'Personal', 28.00, 15, '20 min'),
(81, 'Personal', 30.00, 15, '20 min'),
(82, 'Personal', 35.00, 12, '22 min'),
(83, 'Personal', 28.00, 15, '18 min'),
(84, 'Personal', 30.00, 15, '20 min'),
(85, 'Personal', 30.00, 15, '20 min'),
(86, 'Personal', 30.00, 12, '20 min'),
(87, 'Personal', 28.00, 15, '20 min'),
(88, 'Personal', 30.00, 12, '22 min'),
(89, 'Personal', 30.00, 12, '22 min'),
(90, 'Personal', 30.00, 15, '20 min'),
(91, 'Personal', 35.00, 12, '25 min'),
(92, 'Personal', 35.00, 12, '25 min'),
(93, 'Personal', 35.00, 12, '25 min');

INSERT INTO productos (id_producto, id_categoria, nombre) VALUES
(94, 12, 'Causa Acevichada'),
(95, 12, 'Causa de Langostinos'),
(96, 12, 'Causa de Pulpa de Cangrejo'),
(97, 12, 'Causa de Pollo'),
(98, 12, 'Papa a la Huancaina');

INSERT INTO productos_precios (id_producto, tamano, precio, stock, tiempo_preparacion) VALUES
(94, 'Personal', 25.00, 15, '10 min'),
(95, 'Personal', 30.00, 12, '12 min'),
(96, 'Personal', 30.00, 12, '12 min'),
(97, 'Personal', 20.00, 15, '10 min'),
(98, 'Personal', 10.00, 20, '8 min');
