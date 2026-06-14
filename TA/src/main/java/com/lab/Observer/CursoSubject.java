package com.lab.Observer;

import com.lab.Models.Curso;
import java.util.ArrayList;
import java.util.List;

public class CursoSubject {
    private static CursoSubject instancia;
    private List<CursoObserver> observers;
    
    private CursoSubject() {
        this.observers = new ArrayList<>();
    }
    
    public static CursoSubject getInstancia() {
        if (instancia == null) {
            instancia = new CursoSubject();
        }
        return instancia;
    }
    
    public void registrarObserver(CursoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void notificarCursoAgregado(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoAgregado(curso);
        }
    }
    
    public void notificarCursoEliminado(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoEliminado(curso);
        }
    }
    
    public void notificarCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo) {
        for (CursoObserver observer : observers) {
            observer.onCursoActualizado(cursoAntiguo, cursoNuevo);
        }
    }
}