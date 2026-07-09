-- =========================================================
-- SISTEMA DE MATRÍCULA DE CURSOS EXTRACURRICULARES
-- Instituto Tecnológico Valle Grande
-- Compatible con MySQL 8.4
-- =========================================================

DROP DATABASE IF EXISTS hackathon_matricula;

CREATE DATABASE hackathon_matricula
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hackathon_matricula;

-- =========================================================
-- TABLA: CURSO
-- =========================================================

CREATE TABLE curso (
                       id_curso INT AUTO_INCREMENT PRIMARY KEY,
                       nombre_curso VARCHAR(100) NOT NULL UNIQUE,
                       descripcion VARCHAR(255),
                       cupo_maximo INT NOT NULL DEFAULT 20,
                       cupo_disponible INT NOT NULL DEFAULT 20,
                       estado TINYINT NOT NULL DEFAULT 1
);

INSERT INTO curso (
    nombre_curso,
    descripcion,
    cupo_maximo,
    cupo_disponible
)
VALUES
    ('Liderazgo', 'Taller de liderazgo estudiantil', 20, 20),
    ('Oratoria', 'Taller de oratoria y debate', 20, 20),
    ('Python Básico', 'Introducción a Python', 25, 25),
    ('Cloud Computing', 'Fundamentos de Cloud Computing', 25, 25);

-- =========================================================
-- TABLA: USUARIO
-- =========================================================

CREATE TABLE usuario (
                         id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(100) NOT NULL,
                         nombre_completo VARCHAR(100)
);

INSERT INTO usuario (
    username,
    password,
    nombre_completo
)
VALUES
    ('admin', 'admin123', 'Administrador Académico');

-- =========================================================
-- TABLA: MATRICULA
-- =========================================================

CREATE TABLE matricula (
                           id_matricula INT AUTO_INCREMENT PRIMARY KEY,
                           dni CHAR(8) NOT NULL,
                           nombres VARCHAR(100) NOT NULL,
                           apellidos VARCHAR(100) NOT NULL,
                           id_curso INT NOT NULL,
                           turno ENUM('MAÑANA', 'TARDE', 'NOCHE') NOT NULL,
                           beca BOOLEAN NOT NULL DEFAULT FALSE,
                           monto_pago DECIMAL(8,2) NOT NULL DEFAULT 0,
                           fecha_matricula DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           estado TINYINT NOT NULL DEFAULT 1,

                           CONSTRAINT fk_matricula_curso
                               FOREIGN KEY (id_curso)
                                   REFERENCES curso(id_curso)
);

-- =========================================================
-- ELIMINAR TRIGGERS SI EXISTEN
-- =========================================================

DROP TRIGGER IF EXISTS trg_before_insert_matricula;
DROP TRIGGER IF EXISTS trg_after_insert_matricula;
DROP TRIGGER IF EXISTS trg_after_update_matricula;

DELIMITER $$

-- =========================================================
-- TRIGGER: VALIDAR CUPOS ANTES DE MATRICULAR
-- =========================================================

CREATE TRIGGER trg_before_insert_matricula
    BEFORE INSERT ON matricula
    FOR EACH ROW
BEGIN
    DECLARE disponible INT;

    SELECT cupo_disponible
    INTO disponible
    FROM curso
    WHERE id_curso = NEW.id_curso
      AND estado = 1;

    IF disponible IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El curso no existe o está inactivo';
END IF;

IF disponible <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay cupos disponibles';
END IF;
END$$

-- =========================================================
-- TRIGGER: DESCONTAR CUPOS AL MATRICULAR
-- =========================================================

CREATE TRIGGER trg_after_insert_matricula
    AFTER INSERT ON matricula
    FOR EACH ROW
BEGIN
    UPDATE curso
    SET cupo_disponible = cupo_disponible - 1
    WHERE id_curso = NEW.id_curso;
    END$$

    -- =========================================================
-- TRIGGER: DEVOLVER CUPOS AL ANULAR MATRÍCULA
-- =========================================================

    CREATE TRIGGER trg_after_update_matricula
        AFTER UPDATE ON matricula
        FOR EACH ROW
    BEGIN
        IF OLD.estado = 1
       AND NEW.estado = 0 THEN

        UPDATE curso
        SET cupo_disponible = cupo_disponible + 1
        WHERE id_curso = NEW.id_curso;

    END IF;
    END$$

    DELIMITER ;

-- =========================================================
-- DATOS DE PRUEBA
-- =========================================================

    INSERT INTO matricula (
        dni,
        nombres,
        apellidos,
        id_curso,
        turno,
        beca,
        monto_pago
    )
    VALUES (
               '12345678',
               'Oscar',
               'Sanchez',
               1,
               'MAÑANA',
               FALSE,
               150.00
           );

    -- =========================================================
-- CONSULTAS DE VERIFICACIÓN
-- =========================================================

    SELECT * FROM curso;
    SELECT * FROM usuario;
    SELECT * FROM matricula;
    SHOW TRIGGERS;