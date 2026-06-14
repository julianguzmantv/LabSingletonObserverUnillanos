package com.lab.Observer;

import com.lab.Models.Curso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsolaMonitoreoObserver implements CursoObserver {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onCursoAgregado(Curso curso) {
        String mensaje = String.format("[%s] 📗 CURSO AGREGADO | ID: %.0f | %s",
                dateFormat.format(new Date()),
                curso.getId(),
                curso.getNombre());
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
        String mensaje = String.format("[%s] 📘 CURSO ACTUALIZADO | ID: %.0f | %s",
                dateFormat.format(new Date()),
                cursoAntiguo.getId(),
                cursoNuevo.getNombre());
        System.out.println(mensaje);
    }

    @Override
    public void onInscripcionCreada(String mensaje) {
    }
}