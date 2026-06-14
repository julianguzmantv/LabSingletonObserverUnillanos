package com.lab.Observer;

import com.lab.Models.Curso;

public interface CursoObserver {
    void onCursoAgregado(Curso curso);
    void onCursoEliminado(Curso curso);
    void onCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo);
    void onInscripcionCreada(String mensaje);
}