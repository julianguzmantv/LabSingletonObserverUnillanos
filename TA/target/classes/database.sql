CREATE TABLE IF NOT EXISTS profesor (
    id DOUBLE PRIMARY KEY,
    nombre varchar(100) NOT NULL,
    apellidos varchar(100) NOT NULL,
    email varchar(200) NOT NULL,
    tipo_contrato VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS curso (
    id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    programa_id DOUBLE,
    activo BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS estudiante (
    id DOUBLE PRIMARY KEY,
    nombre varchar(100) NOT NULL,
    apellidos varchar(100) NOT NULL,
    email varchar(200) NOT NULL,
    codigo DOUBLE NOT NULL,
    programa_id DOUBLE,
    activo BOOLEAN NOT NULL,
    promedio DOUBLE
);

CREATE TABLE IF NOT EXISTS inscripcion (
    id DOUBLE PRIMARY KEY,
    curso_id INT NOT NULL,
    ano INT NOT NULL,
    semestre INT NOT NULL,
    estudiante_id DOUBLE NOT NULL,

    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso_id) REFERENCES curso(id),
    CONSTRAINT fk_inscripcion_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(id)
);

INSERT INTO profesor (id, nombre, apellido, email, tipo_contrato) VALUES
(1, 'Roger', 'Martinez', 'roger.martinez@unillanos.edu.co', 'PLanta'),
(2, 'Julian', 'Ríos', 'julian.rios@unillanos.edu.co', 'Catedrático'),
(3, 'Julian', 'Romero', 'julian.romero@unillanos.edu.co', 'Catedrático');

INSERT INTO curso (id, nombre, programa_id, activo) VALUES
(01, 'Cálculo Dif.', 1, TRUE),
(02, 'Física', 1, TRUE),
(03, 'Programación Orientada a Objetos', 1, TRUE),

INSERT INTO estudiante (id, nombre, apellido, email, codigo, programa_id, activo, promedio) VALUES
(001, 'Pepito', 'Pérez', 'pepito,perez@unillanos.edu.co', 160001, 1, TRUE, 3.7),
(002, 'Carlitos', 'Rodríguez', 'carlitos.rodriguez@unillanos.edu.co', 160002, 1, TRUE, 4.2),
(003, 'Sara', 'Castillo', 'sara.castillo@unillanos.edu.co', 160003, 1, TRUE, 3.0),

INSERT INTO inscripcion (id, curso_id, ano, semestre, estudiante_id) VALUES
(5001, 01, 2025, 1, 001),
(5002, 02, 2025, 1, 002),
(5003, 03, 2025, 2, 003),
