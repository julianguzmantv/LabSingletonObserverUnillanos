package com.lab.Observer;
import com.lab.Observer.CursoObserver;
import com.lab.Observer.CursoSubject;
import com.lab.Models.Curso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsolaMonitoreo {
    
    static class MonitorObserver implements CursoObserver {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void onCursoAgregado(Curso curso) {
            System.out.printf("[%s] 📗 CURSO AGREGADO | ID: %.0f | %s%n",
                    dateFormat.format(new Date()), curso.getId(), curso.getNombre());
        }

        @Override
        public void onCursoEliminado(Curso curso) {
            System.out.printf("[%s] 📕 CURSO ELIMINADO | ID: %.0f | %s%n",
                    dateFormat.format(new Date()), curso.getId(), curso.getNombre());
        }

        @Override
        public void onCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo) {
            System.out.printf("[%s] 📘 CURSO ACTUALIZADO | ID: %.0f | %s%n",
                    dateFormat.format(new Date()), cursoAntiguo.getId(), cursoNuevo.getNombre());
        }

        @Override
        public void onInscripcionCreada(String mensaje) {
        }
    }
    
    public static void main(String[] args) {
        System.out.println("📊 CONSOLA DE MONITOREO - OBSERVER");
        System.out.println("==================================");
        MonitorObserver observer = new MonitorObserver();
        CursoSubject.getInstancia().registrarObserver(observer);
        System.out.println("🟢 OBSERVER REGISTRADO - Monitoreo ACTIVO");
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