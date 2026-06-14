package com.lab.Controller;

import javax.swing.JOptionPane;

import com.lab.DataAccessObj.CursosInscritosDAO;
import com.lab.Interfaces.InterfazMain;
import com.lab.Models.Curso;
import com.lab.Models.Estudiante;
import com.lab.Models.Inscripcion;
import com.lab.Observer.CursoSubject;

public class ControladorCursos {
    private static final ControladorCursos INSTANCIA = new ControladorCursos();
    
    private InterfazMain interfazMain;
    private CursosInscritosDAO cursosInscritosDAO;
    private boolean inicializado = false;
    private CursoSubject cursoSuj;

    //singleton
    private ControladorCursos() {
        if (INSTANCIA != null) {
            throw new IllegalStateException("Ya existe una instancia de ControladorCursos");
        }
        this.cursoSuj = CursoSubject.getInstancia();
    }

    public static ControladorCursos getInstancia() {
        return INSTANCIA;
    }

    public void inicializar(InterfazMain interfazMain, CursosInscritosDAO cursosInscritosDAO) {
        if (!inicializado) {
            this.interfazMain = interfazMain;
            this.cursosInscritosDAO = cursosInscritosDAO;
            this.interfazMain.addInscribirListener(e -> inscribirCurso());
            this.inicializado = true;
        } else {
            throw new IllegalStateException("ControladorCursos ya ha sido inicializado");
        }
    }
    
    private void inscribirCurso() {
    try {
        Curso cursoSeleccionado = interfazMain.getCursoSeleccionado();
        Estudiante estudianteSeleccionado = interfazMain.getEstudianteSeleccionado();
        int ano = interfazMain.getAno();
        int semestre = interfazMain.getSemestre();

        if (cursoSeleccionado == null || estudianteSeleccionado == null) {
            JOptionPane.showMessageDialog(interfazMain, "Por favor, seleccione un curso y un estudiante.",
                    "Error de Selección", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ano <= 0 || semestre <= 0) {
            JOptionPane.showMessageDialog(interfazMain, "Por favor, ingrese valores válidos para el año y semestre.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double idInscripcion = cursosInscritosDAO.obtenerSiguienteId();
        Inscripcion nuevaInscripcion = new Inscripcion(idInscripcion, cursoSeleccionado, ano, semestre,
                estudianteSeleccionado);

        boolean exito = cursosInscritosDAO.guardar(nuevaInscripcion);
        
        if (exito) {
            interfazMain.refrescarTabla();
            interfazMain.limpiarCampos();

            JOptionPane.showMessageDialog(interfazMain, 
                "Inscripción creada correctamente.\nID: " + idInscripcion,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            System.out.println("✅ Inscripción completada - Tabla refrescada");
        } else {
            JOptionPane.showMessageDialog(interfazMain, 
                "No se pudo crear la inscripción. Verifique que el estudiante no esté ya inscrito.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(interfazMain, 
            "Ocurrió un error al inscribir el curso: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    public void notificarCursoAgregado(Curso curso) {
        cursoSuj.notificarCursoAgregado(curso);
    }

    public void notificarCursoEliminado(Curso curso) {
        cursoSuj.notificarCursoEliminado(curso);
    }

    public void notificarCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo) {
        cursoSuj.notificarCursoActualizado(cursoAntiguo, cursoNuevo);
    }

    public boolean estaInicializado() {
        return inicializado;
    }

    public static void reset() {
        try {
            java.lang.reflect.Field field = ControladorCursos.class.getDeclaredField("INSTANCIA");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}