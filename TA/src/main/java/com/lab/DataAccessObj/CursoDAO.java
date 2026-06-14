package com.lab.DataAccessObj;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.lab.Models.Curso;
import com.lab.Observer.CursoSubject;
import com.lab.connections.DBConnect;

public class CursoDAO {
    private final DBConnect conexionDB;
    private CursoSubject cursoSubject;

    public CursoDAO(DBConnect conexionDB) {
        this.conexionDB = conexionDB;
        this.cursoSubject = CursoSubject.getInstancia();
    }

    public List<Curso> obtenerTodos() {
        List<Curso> lista = new ArrayList<>();
        try (Statement stmt = conexionDB.getConexion().getConexion().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM curso ORDER BY id")) {
            while (rs.next()) {
                Double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");
                boolean activo = rs.getBoolean("activo");

                lista.add(new Curso(id, nombre, null, activo));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cursos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public Curso buscarPorId(Double id) {
        Curso curso = null;
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion()
                .prepareStatement("SELECT * FROM CURSO WHERE id = ?")) {
            pstmt.setDouble(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Double cursoId = rs.getDouble("id");
                    String nombre = rs.getString("nombre");
                    boolean activo = rs.getBoolean("activo");

                    curso = new Curso(cursoId, nombre, null, activo);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar curso por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return curso;
    }

    public void insertar(Curso curso) {
        String sql = "INSERT INTO curso (id, nombre, activo) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, curso.getId());
            pstmt.setString(2, curso.getNombre());
            pstmt.setBoolean(3, curso.getActivo());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                cursoSubject.notificarCursoAgregado(curso);
                System.out.println("✅ Curso insertado: " + curso.getNombre());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizar(Curso cursoAntiguo, Curso cursoNuevo) {
        String sql = "UPDATE curso SET nombre = ?, activo = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setString(1, cursoNuevo.getNombre());
            pstmt.setBoolean(2, cursoNuevo.getActivo());
            pstmt.setDouble(3, cursoNuevo.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                cursoSubject.notificarCursoActualizado(cursoAntiguo, cursoNuevo);
                System.out.println("✅ Curso actualizado: " + cursoNuevo.getNombre());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminar(Curso curso) {
        String sql = "DELETE FROM curso WHERE id = ?";
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, curso.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                cursoSubject.notificarCursoEliminado(curso);
                System.out.println("✅ Curso eliminado: " + curso.getNombre());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean existeCurso(Double id) {
        return buscarPorId(id) != null;
    }

    public List<Curso> obtenerCursosActivos() {
        List<Curso> lista = new ArrayList<>();
        try (Statement stmt = conexionDB.getConexion().getConexion().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM curso WHERE activo = true ORDER BY nombre")) {
            while (rs.next()) {
                Double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");
                boolean activo = rs.getBoolean("activo");

                lista.add(new Curso(id, nombre, null, activo));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cursos activos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}