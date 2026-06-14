package com.lab.DataAccessObj;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.lab.Models.Estudiante;
import com.lab.Models.Programa;
import com.lab.connections.DBConnect;

public class EstudianteDAO {
    private final DBConnect conexionDB;

    public EstudianteDAO(DBConnect conexionDB) {
        this.conexionDB = conexionDB;
    }

    public void insertar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiante (id, nombre, apellidos, email, codigo, programa_id, activo, promedio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, estudiante.getId());
            pstmt.setString(2, estudiante.getNombre());
            pstmt.setString(3, estudiante.getApellidos());
            pstmt.setString(4, estudiante.getEmail());
            pstmt.setDouble(5, estudiante.getCodigo());

            // Si el programa existe, inserta su ID. De lo contrario, inserta null.
            if (estudiante.getPrograma() != null) {
                pstmt.setDouble(6, estudiante.getPrograma().getId());
            } else {
                pstmt.setNull(6, java.sql.Types.DOUBLE);
            }

            pstmt.setBoolean(7, estudiante.getActivo());
            pstmt.setDouble(8, estudiante.getPromedio());
            pstmt.executeUpdate();
            System.out.println("Estudiante insertado: " + estudiante.getNombre());
        } catch (SQLException e) {
            System.err.println("Error al insertar estudiante: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiante";
        try (Statement stmt = conexionDB.getConexion().getConexion().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante estudiante = mapearEstudiante(rs);
                lista.add(estudiante);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los estudiantes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public Estudiante buscarPorId(Double id) {
        Estudiante estudiante = null;
        String sql = "SELECT * FROM estudiante WHERE id = ?";
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estudiante = mapearEstudiante(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudiante por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return estudiante;
    }

    public void actualizar(Estudiante estudiante) {
            String sql = "UPDATE estudiante SET nombre = ?, apellidos = ?, email = ?, codigo = ?, programa_id = ?, activo = ?, promedio = ? "
                    +
                    "WHERE id = ?";
            try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
                pstmt.setString(1, estudiante.getNombre());
                pstmt.setString(2, estudiante.getApellidos());
                pstmt.setString(3, estudiante.getEmail());
                pstmt.setDouble(4, estudiante.getCodigo());

                if (estudiante.getPrograma() != null) {
                    pstmt.setDouble(5, estudiante.getPrograma().getId());
                } else {
                    pstmt.setNull(5, java.sql.Types.DOUBLE);
                }

                pstmt.setBoolean(6, estudiante.getActivo());
                pstmt.setDouble(7, estudiante.getPromedio());
                pstmt.setDouble(8, estudiante.getId());
                pstmt.executeUpdate();
                System.out.println("Estudiante actualizado: " + estudiante.getNombre());
            } catch (SQLException e) {
                System.err.println("Error al actualizar estudiante: " + e.getMessage());
                e.printStackTrace();
            }
        }

    public void eliminar(Double id) {
        Connection conn = null;
        try {
            conn = conexionDB.getConexion().getConexion();
            conn.setAutoCommit(false);
            
            String sqlInscripciones = "DELETE FROM inscripcion WHERE estudiante_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInscripciones)) {
                pstmt.setDouble(1, id);
                pstmt.executeUpdate();
            }
            
            String sqlEstudiante = "DELETE FROM estudiante WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEstudiante)) {
                pstmt.setDouble(1, id);
                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Estudiante con ID " + id + " eliminado.");
                } else {
                    System.out.println("No se encontró estudiante con ID " + id);
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String email = rs.getString("email");
        Double codigo = rs.getDouble("codigo");
        boolean activo = rs.getBoolean("activo");
        Double promedio = rs.getDouble("promedio");

        Double programaId = rs.getDouble("programa_id");
        Programa programa = new Programa(programaId, null, null, null, null); 

        return new Estudiante(id, nombres, apellidos, email, codigo, programa, activo, promedio);
    }
}