package com.lab.Factory;

import com.lab.DataAccessObj.CursoDAO;
import com.lab.DataAccessObj.CursosInscritosDAO;
import com.lab.DataAccessObj.EstudianteDAO;
import com.lab.connections.DBConnect;

public class H2DAO implements FactoryDAO {
    private final DBConnect conexionDB;
    public H2DAO() {
        this.conexionDB = DBConnect.getInstancia();
    }
    public H2DAO(DBConnect conexionDB) {
        if (conexionDB != DBConnect.getInstancia()) {
            System.out.println("Advertencia: H2DAO no está usando la instancia singleton");
        }
        this.conexionDB = conexionDB;
    }

    @Override
    public CursoDAO createCursoDAO() {
        return new CursoDAO(conexionDB);
    }

    @Override
    public EstudianteDAO createEstudianteDAO() {
        return new EstudianteDAO(conexionDB);
    }

    @Override
    public CursosInscritosDAO createInscripcionesDAO() {
        CursoDAO cursoDAO = createCursoDAO();
        EstudianteDAO estudianteDAO = createEstudianteDAO();
        
        return new CursosInscritosDAO(conexionDB, cursoDAO, estudianteDAO);
    }
}