package com.lab.connections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectH2DB implements Connect {
    private Connection connection;

    public ConnectH2DB() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager
                    .getConnection("jdbc:h2:~/gestorCursos;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=TRUE", "sa", "");
            inicializarTablas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarTablas() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS profesor (\r\n" +
                    "    id DOUBLE PRIMARY KEY,\r\n" +
                    "    nombre varchar(100) NOT NULL,\r\n" +
                    "    apellidos varchar(100) NOT NULL,\r\n" +
                    "    email varchar(200) NOT NULL,\r\n" +
                    "    tipo_contrato VARCHAR(50) NOT NULL\r\n" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS curso (\r\n" +
                    "    id INT PRIMARY KEY,\r\n" +
                    "    nombre VARCHAR(100) NOT NULL,\r\n" +
                    "    programa_id DOUBLE,\r\n" +
                    "    activo BOOLEAN NOT NULL\r\n" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS estudiante (\r\n" +
                    "    id DOUBLE PRIMARY KEY,\r\n" +
                    "    nombre varchar(100) NOT NULL,\r\n" +
                    "    apellidos varchar(100) NOT NULL,\r\n" +
                    "    email varchar(200) NOT NULL,\r\n" +
                    "    codigo DOUBLE NOT NULL,\r\n" +
                    "    programa_id DOUBLE,\r\n" +
                    "    activo BOOLEAN NOT NULL,\r\n" +
                    "    promedio DOUBLE\r\n" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS inscripcion(\r\n" +
                    "    id DOUBLE PRIMARY KEY,\r\n" +
                    "    curso_id INT NOT NULL,\r\n" +
                    "    ano INT NOT NULL,\r\n" +
                    "    semestre INT NOT NULL,\r\n" +
                    "    estudiante_id DOUBLE NOT NULL,\r\n" +
                    "    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso_id) REFERENCES curso(id),\r\n" +
                    "    CONSTRAINT fk_inscripcion_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(id)\r\n"
                    +
                    ")");
            statement.execute("MERGE INTO curso (id, nombre, programa_id, activo) VALUES\r\n" +
                    "(01, 'Cálculo Diferencial', 1, TRUE),\r\n" +
                    "(02, 'POO', 1, TRUE),\r\n" +
                    "(03, 'Tecnologías AVanzadas', 1, TRUE),\r\n" +
                    "(04, 'Proyecto de Grado', 2, FALSE)");
            statement.execute(
                    "MERGE INTO estudiante (id, nombre, apellidos, email, codigo, programa_id, activo, promedio) VALUES\r\n" +
                            "(1001, 'Juan', 'Rodriguez', 'juan.rodriguez@unillanos.edu.co', 2023001, 1, TRUE, 4.2),\r\n" +
                            "(1002, 'Roger', 'Ramos', 'santiago.perez@unillanos.edu.co', 2023002, 1, TRUE, 3.8),\r\n" +
                            "(1003, 'Julian', 'Romero', 'maria.lopez@unillanos.edu.co', 2023003, 1, TRUE, 4.5)"); 
            statement.execute("MERGE INTO inscripcion (id, curso_id, ano, semestre, estudiante_id) VALUES\r\n" +
                    "(001, 101, 2025, 1, 1001),\r\n" +
                    "(002, 102, 2025, 1, 1002),\r\n" +
                    "(003, 101, 2020, 1, 1005);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return connection;
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCurrentDateTimeQuery() {
        return "SELECT NOW()";
    }
}
