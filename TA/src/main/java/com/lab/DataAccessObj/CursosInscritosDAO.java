package com.lab.DataAccessObj;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.lab.DataTransferObj.InscripcionDTO;
import com.lab.Models.Curso;
import com.lab.Models.Estudiante;
import com.lab.Models.Inscripcion;
import com.lab.connections.DBConnect;

public class CursosInscritosDAO {
    private final DBConnect conexionDB;
    private CursoDAO cursoDAO;
    private EstudianteDAO estudianteDAO;

    public CursosInscritosDAO(DBConnect conexionDB, CursoDAO cursoDAO, EstudianteDAO estudianteDAO) {
        this.conexionDB = conexionDB;
        this.cursoDAO = cursoDAO;
        this.estudianteDAO = estudianteDAO;
    }

    public boolean guardar(Inscripcion inscripcion) {
        // Validar que no exista ya la inscripción
        if (existeInscripcion(inscripcion.getEstudiante().getId(), inscripcion.getCurso().getId(), 
                             inscripcion.getAno(), inscripcion.getSemestre())) {
            System.err.println("Error: El estudiante ya está inscrito en este curso para el año " + 
                             inscripcion.getAno() + " y semestre " + inscripcion.getSemestre());
            return false;
        }

        String sql = "INSERT INTO INSCRIPCION (id, curso_id, ano, semestre, estudiante_id) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, inscripcion.getId());
            pstmt.setDouble(2, inscripcion.getCurso().getId());
            pstmt.setInt(3, inscripcion.getAno());
            pstmt.setInt(4, inscripcion.getSemestre());
            pstmt.setDouble(5, inscripcion.getEstudiante().getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✅ Inscripción guardada exitosamente: " + 
                                 inscripcion.getEstudiante().getNombre() + " en " + 
                                 inscripcion.getCurso().getNombre());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar inscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private boolean existeInscripcion(Double estudianteId, Double cursoId, int ano, int semestre) {
        String sql = "SELECT COUNT(*) as count FROM INSCRIPCION WHERE estudiante_id = ? AND curso_id = ? AND ano = ? AND semestre = ?";
        
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, estudianteId);
            pstmt.setDouble(2, cursoId);
            pstmt.setInt(3, ano);
            pstmt.setInt(4, semestre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar inscripción existente: " + e.getMessage());
        }
        return false;
    }

    public void eliminar(Double id) {
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion()
                .prepareStatement("DELETE FROM INSCRIPCION WHERE id = ?")) {
            pstmt.setDouble(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✅ Inscripción eliminada: ID " + id);
            } else {
                System.out.println("⚠️ No se encontró inscripción con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizar(Inscripcion inscripcion) {
        String sql = "UPDATE INSCRIPCION SET curso_id = ?, ano = ?, semestre = ?, estudiante_id = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, inscripcion.getCurso().getId());
            pstmt.setInt(2, inscripcion.getAno());
            pstmt.setInt(3, inscripcion.getSemestre());
            pstmt.setDouble(4, inscripcion.getEstudiante().getId());
            pstmt.setDouble(5, inscripcion.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✅ Inscripción actualizada: ID " + inscripcion.getId());
            } else {
                System.out.println("⚠️ No se encontró inscripción con ID: " + inscripcion.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Double obtenerSiguienteId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM INSCRIPCION";
        
        try (Statement stmt = conexionDB.getConexion().getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Double nextId = rs.getDouble("next_id");
                System.out.println("🔢 Siguiente ID de inscripción: " + nextId);
                return nextId;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener siguiente ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 1.0;
    }

    public List<InscripcionDTO> cargarTodos() {
        List<InscripcionDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM INSCRIPCION ORDER BY ano DESC, semestre DESC, id DESC";
        
        try (Statement stmt = conexionDB.getConexion().getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int contador = 0;
            while (rs.next()) {
                Double id = rs.getDouble("id");
                Double curso_id = rs.getDouble("curso_id");
                int ano = rs.getInt("ano");
                int semestre = rs.getInt("semestre");
                Double estudiante_id = rs.getDouble("estudiante_id");

                Curso curso = cursoDAO.buscarPorId(curso_id);
                Estudiante estudiante = estudianteDAO.buscarPorId(estudiante_id);

                if(curso != null && estudiante != null){
                    lista.add(new InscripcionDTO(id, curso.getId(), curso.getNombre(), 
                                                estudiante.getId(), estudiante.getNombre(), 
                                                estudiante.getApellidos(), ano, semestre));
                    contador++;
                } else {
                    System.err.println("⚠️ Inscripción con datos inconsistentes - ID: " + id);
                }
            }
            
            System.out.println("📋 Cargadas " + contador + " inscripciones");
            
        } catch (SQLException e) {
            System.err.println("❌ Error al cargar inscripciones: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public Inscripcion buscarPorId(Double id) {
        String sql = "SELECT * FROM INSCRIPCION WHERE id = ?";
        
        try (PreparedStatement pstmt = conexionDB.getConexion().getConexion().prepareStatement(sql)) {
            pstmt.setDouble(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Double curso_id = rs.getDouble("curso_id");
                    int ano = rs.getInt("ano");
                    int semestre = rs.getInt("semestre");
                    Double estudiante_id = rs.getDouble("estudiante_id");

                    Curso curso = cursoDAO.buscarPorId(curso_id);
                    Estudiante estudiante = estudianteDAO.buscarPorId(estudiante_id);

                    if (curso != null && estudiante != null) {
                        return new Inscripcion(id, curso, ano, semestre, estudiante);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar inscripción por ID: " + e.getMessage());
        }
        return null;
    }
}