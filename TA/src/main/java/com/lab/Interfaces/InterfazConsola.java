package com.lab.Interfaces;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import com.lab.DataAccessObj.CursoDAO;
import com.lab.DataAccessObj.EstudianteDAO;
import com.lab.Factory.DatesDB;
import com.lab.Models.Curso;
import com.lab.Models.Estudiante;
import com.lab.connections.DBConnect;

public class InterfazConsola extends Thread {

    private DBConnect conexion;
    private EstudianteDAO estudianteDAO;
    private CursoDAO cursoDAO;
    private volatile boolean activo;
    private Scanner scanner;

    public InterfazConsola(DBConnect conexion, EstudianteDAO estudianteDAO, CursoDAO cursoDAO) {
        this.conexion = conexion;
        this.estudianteDAO = estudianteDAO;
        this.cursoDAO = cursoDAO;
        this.activo = true;
        this.scanner = new Scanner(System.in);
        setDaemon(true);
    }

    @Override
    public void run() {
        System.out.println("🎯 Menú Administrativo Listo - Escriba opciones abajo:");
        System.out.println("=========================================");
        
        while (activo) {
            mostrarMenu();

            try {
                System.out.print("Opción: ");
                String input = scanner.nextLine();
                
                if (input.trim().isEmpty()) {
                    continue; 
                }
                
                int opcion = Integer.parseInt(input);
                procesarOpcion(opcion);
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido (1-4)\n");
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage() + "\n");
            }
        }

        scanner.close();
        System.out.println("👋 Consola administrativa cerrada");
    }

    private void mostrarMenu() {
        System.out.println("\nMENÚ DE SELECCIÓN POR CONSOLA");
        System.out.println("1. Mostrar FECHA BASE DE DATOS");
        System.out.println("2. Mostrar lista de estudiantes");
        System.out.println("3. Mostrar lista de cursos");
        System.out.println("4. Agregar nuevo curso");
        System.out.println("5. Actualizar curso");
        System.out.println("6. Eliminar curso");
        System.out.println("7. Abandonar menú de consola");
    }

    private void procesarOpcion(int opcion) {
    System.out.println(); 

        switch (opcion) {
            case 1: mostrarFechaBD(); break;
            case 2: listarEstudiantes(); break;
            case 3: listarCursos(); break;
            case 4: agregarCurso(); break;
            case 5: actualizarCurso(); break;
            case 6: eliminarCurso(); break;
            case 7: salir(); break;
            default: System.out.println("Opcion invalida. Intente de nuevo.\n");
        }
    }

    private void mostrarFechaBD() {
        try {
            Timestamp fechaBD = DatesDB.getCurrentDateTime(
                    conexion.getConexion().getConexion());
            System.out.println("Fecha de la base de datos: " + fechaBD);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error al obtener fecha: " + e.getMessage() + "\n");
        }
    }

    private void listarEstudiantes() {
        try {
            List<Estudiante> estudiantes = estudianteDAO.obtenerTodos();

            if (estudiantes.isEmpty()) {
                System.out.println("No hay estudiantes registrados.\n");
                return;
            }

            System.out.println("LISTA DE ESTUDIANTES:");
            System.out.println("─────────────────────────────────────────");
            for (Estudiante est : estudiantes) {
                String programa = est.getPrograma() != null ? est.getPrograma().getNombre() : "Sin programa";
                String estado = est.isActivo() ? "Activo" : "Inactivo";
                System.out.printf(
                        "ID: %.0f | Codigo: %.0f | Nombre: %s %s | Promedio: %.2f | Estado: %s%n",
                        est.getId(),
                        est.getCodigo(),
                        est.getNombre(),
                        est.getApellidos(),
                        est.getPromedio(),
                        estado);
            }
            System.out.println("─────────────────────────────────────────");
            System.out.println("Hay: " + estudiantes.size() + " estudiantes inscritos\n");

        } catch (Exception e) {
            System.out.println("Error al mostrar lista de estudiantes: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void listarCursos() {
        try {
            List<Curso> cursos = cursoDAO.obtenerTodos();

            if (cursos.isEmpty()) {
                System.out.println("No hay cursos registrados.\n");
                return;
            }

            System.out.println("LISTA DE CURSOS:");
            System.out.println("─────────────────────────────────────────");
            for (Curso c : cursos) {
                String estado = c.isActivo() ? "Activo" : "Inactivo";
                System.out.printf("ID: %.0f | Nombre: %s | Estado: %s%n",
                        c.getId(),
                        c.getNombre(),
                        estado);
            }
            System.out.println("─────────────────────────────────────────");
            System.out.println("Total: " + cursos.size() + " cursos\n");

        } catch (Exception e) {
            System.out.println("Error al listar cursos: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void agregarCurso() {
        try {
            System.out.println("📗 AGREGAR NUEVO CURSO");
            System.out.print("ID del curso: ");
            Double id = scanner.nextDouble();
            scanner.nextLine(); 
            
            System.out.print("Nombre del curso: ");
            String nombre = scanner.nextLine();
            
            System.out.print("¿Curso activo? (true/false): ");
            boolean activo = scanner.nextBoolean();
            scanner.nextLine(); 
            
            if (cursoDAO.existeCurso(id)) {
                System.out.println("❌ Ya existe un curso con ID: " + id + "\n");
                return;
            }
            
            Curso nuevoCurso = new Curso(id, nombre, null, activo);
            cursoDAO.insertar(nuevoCurso);
            
            System.out.println("✅ Curso agregado exitosamente\n");
            
        } catch (Exception e) {
            System.out.println("❌ Error al agregar curso: " + e.getMessage() + "\n");
            scanner.nextLine(); 
        }
    }

    private void actualizarCurso() {
        try {
            System.out.println("📘 ACTUALIZAR CURSO");
            System.out.print("ID del curso a actualizar: ");
            Double id = scanner.nextDouble();
            scanner.nextLine(); 
            
            Curso cursoExistente = cursoDAO.buscarPorId(id);
            if (cursoExistente == null) {
                System.out.println("❌ No existe un curso con ID: " + id + "\n");
                return;
            }
            
            System.out.println("Curso actual: " + cursoExistente.getNombre() + 
                            " | Activo: " + cursoExistente.isActivo());
            
            System.out.print("Nuevo nombre (enter para mantener actual): ");
            String nuevoNombre = scanner.nextLine();
            if (nuevoNombre.trim().isEmpty()) {
                nuevoNombre = cursoExistente.getNombre();
            }
            
            System.out.print("¿Curso activo? (true/false, enter para mantener actual): ");
            String activoInput = scanner.nextLine();
            boolean nuevoActivo = cursoExistente.isActivo();
            if (!activoInput.trim().isEmpty()) {
                nuevoActivo = Boolean.parseBoolean(activoInput);
            }
            
            Curso cursoActualizado = new Curso(id, nuevoNombre, null, nuevoActivo);
            
            cursoDAO.actualizar(cursoExistente, cursoActualizado);
            
            System.out.println("✅ Curso actualizado exitosamente\n");
            
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar curso: " + e.getMessage() + "\n");
            scanner.nextLine(); 
        }
    }

    private void eliminarCurso() {
        try {
            System.out.println("📕 ELIMINAR CURSO");
            System.out.print("ID del curso a eliminar: ");
            Double id = scanner.nextDouble();
            scanner.nextLine(); 
            
            Curso curso = cursoDAO.buscarPorId(id);
            if (curso == null) {
                System.out.println("❌ No existe un curso con ID: " + id + "\n");
                return;
            }
            
            System.out.print("¿Está seguro de eliminar el curso: " + curso.getNombre() + "? (s/n): ");
            String confirmacion = scanner.nextLine();
            
            if (confirmacion.equalsIgnoreCase("s")) {
                cursoDAO.eliminar(curso);
                System.out.println("✅ Curso eliminado exitosamente\n");
            } else {
                System.out.println("❌ Eliminación cancelada\n");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar curso: " + e.getMessage() + "\n");
            scanner.nextLine(); 
        }
    }

    private void salir() {
        System.out.println("Cerrando app consola...");
        activo = false;
    }

    public void detener() {
        activo = false;
    }
}