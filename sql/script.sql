-- =========================================================
-- SISTEMA DE MATRÍCULA DE CURSOS EXTRACURRICULARES
-- Instituto Tecnológico Valle Grande
-- Compatible con MySQL 8.x
-- =========================================================

DROP DATABASE IF EXISTS hackathon_matricula;

CREATE DATABASE hackathon_matricula
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hackathon_matricula;

-- =========================================================
-- TABLA CURSO
-- =========================================================

CREATE TABLE curso (
                       id_curso INT AUTO_INCREMENT PRIMARY KEY,
                       nombre_curso VARCHAR(100) NOT NULL UNIQUE,
                       descripcion VARCHAR(255),
                       cupo_maximo INT NOT NULL DEFAULT 20,
                       cupo_disponible INT NOT NULL DEFAULT 20,
                       estado TINYINT DEFAULT 1
);

INSERT INTO curso
(nombre_curso, descripcion, cupo_maximo, cupo_disponible)
VALUES
    ('Liderazgo','Taller de liderazgo estudiantil',20,20),
    ('Oratoria','Taller de oratoria y debate',20,20),
    ('Python Básico','Introducción a Python',25,25),
    ('Cloud Computing','Fundamentos de Cloud Computing',25,25);

-- =========================================================
-- TABLA USUARIO
-- =========================================================

CREATE TABLE usuario (
                         id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(100) NOT NULL,
                         nombre_completo VARCHAR(100)
);

INSERT INTO usuario
(username,password,nombre_completo)
VALUES
    ('admin','admin123','Administrador Académico');

-- =========================================================
-- TABLA MATRICULA
-- =========================================================

CREATE TABLE matricula (
                           id_matricula INT AUTO_INCREMENT PRIMARY KEY,
                           dni CHAR(8) NOT NULL,
                           nombres VARCHAR(100) NOT NULL,
                           apellidos VARCHAR(100) NOT NULL,

                           id_curso INT NOT NULL,

                           turno ENUM('MAÑANA','TARDE','NOCHE') NOT NULL,

                           beca BOOLEAN DEFAULT FALSE,

                           monto_pago DECIMAL(8,2) DEFAULT 0,

                           fecha_matricula DATETIME DEFAULT CURRENT_TIMESTAMP,

                           estado TINYINT DEFAULT 1,

                           CONSTRAINT fk_matricula_curso
                               FOREIGN KEY(id_curso)
                                   REFERENCES curso(id_curso)
);

-- =========================================================
-- DATOS DE PRUEBA
-- =========================================================

INSERT INTO matricula
(dni,nombres,apellidos,id_curso,turno,beca,monto_pago)
VALUES
    ('12345678','Oscar','Sanchez',1,'MAÑANA',FALSE,150.00);

-- =========================================================
-- CONSULTAS
-- =========================================================

SELECT * FROM curso;

SELECT * FROM usuario;

SELECT * FROM matricula;