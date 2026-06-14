package com.lab.Observer;

import com.lab.Models.Curso;
import java.util.ArrayList;
import java.util.List;

public class ObservadorCursos {
    private static ObservadorCursos instancia;
    private List<CursoObserver> observers;
    
    private ObservadorCursos() {
        this.observers = new ArrayList<>();
    }
    
    public static ObservadorCursos getInstance() {
        if (instancia == null) {
            instancia = new ObservadorCursos();
        }
        return instancia;
    }
    
    public void registrarObserver(CursoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void eliminarObserver(CursoObserver observer) {
        observers.remove(observer);
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
    
    public void notificarInscripcionCreada(String mensaje) {
        for (CursoObserver observer : observers) {
            observer.onInscripcionCreada(mensaje);
        }
    }
    
    public int getCantidadObservers() {
        return observers.size();
    }
}