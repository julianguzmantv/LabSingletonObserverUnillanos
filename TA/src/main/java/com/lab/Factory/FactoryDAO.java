package com.lab.Factory;

import com.lab.DataAccessObj.CursoDAO;
import com.lab.DataAccessObj.CursosInscritosDAO;
import com.lab.DataAccessObj.EstudianteDAO;

public interface FactoryDAO {
    CursoDAO createCursoDAO();
    EstudianteDAO createEstudianteDAO();
    CursosInscritosDAO createInscripcionesDAO();
}