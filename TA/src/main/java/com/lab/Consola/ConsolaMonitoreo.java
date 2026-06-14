package com.lab.Consola;
import com.lab.Observer.CursoSubject;
import com.lab.Observer.CursoObserver;
import com.lab.Models.Curso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsolaMonitoreo {
    
    static class MonitorObserver implements CursoObserver {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void onCursoAgregado(Curso curso) {
            String mensaje = String.format("[%s] 📗 CURSO AGREGADO | ID: %.0f | %s | %s",
                    dateFormat.format(new Date()),
                    curso.getId(),
                    curso.getNombre(),
                    curso.getActivo() ? "Activo" : "Inactivo");
            System.out.println(mensaje);
        }

        @Override
        public void onCursoEliminado(Curso curso) {
            String mensaje = String.format("[%s] 📕 CURSO ELIMINADO | ID: %.0f | %s",
                    dateFormat.format(new Date()),
                    curso.getId(),
                    curso.getNombre());
            System.out.println(mensaje);
        }

        @Override
        public void onCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo) {
            StringBuilder cambios = new StringBuilder();
            
            if (!cursoAntiguo.getNombre().equals(cursoNuevo.getNombre())) {
                cambios.append(String.format("Nombre: '%s'→'%s' ", 
                    cursoAntiguo.getNombre(), cursoNuevo.getNombre()));
            }
            
            if (cursoAntiguo.getActivo() != cursoNuevo.getActivo()) {
                cambios.append(String.format("Activo: %s→%s", 
                    cursoAntiguo.getActivo() ? "Sí" : "No", 
                    cursoNuevo.getActivo() ? "Sí" : "No"));
            }

            String mensaje = String.format("[%s] 📘 CURSO ACTUALIZADO | ID: %.0f | %s",
                    dateFormat.format(new Date()),
                    cursoAntiguo.getId(),
                    cambios.length() > 0 ? cambios.toString() : "Sin cambios visibles");
            
            System.out.println(mensaje);
        }

        @Override
        public void onInscripcionCreada(String mensaje) {
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   CONSOLA DE MONITOREO - OBSERVER");
        System.out.println("=========================================");
        System.out.println("Monitoreando cambios en tiempo real...");
        System.out.println("Patrón Observer activo");
        System.out.println("=========================================\n");
        
        //aqui se registra el observer
        MonitorObserver observer = new MonitorObserver();
        CursoSubject.getInstancia().registrarObserver(observer);
        
        System.out.println("🟢 OBSERVER REGISTRADO - Monitoreo ACTIVO");
        System.out.println("💡 Realiza cambios en cursos desde la Consola Administrativa");
        System.out.println("💡 Las notificaciones aparecerán aquí automáticamente\n");
        
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("🔴 Monitoreo interrumpido");
        }
    }
}