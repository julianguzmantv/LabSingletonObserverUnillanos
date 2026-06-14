//no se ejecuta esta clase es solo para pruebas

package com.lab.Consola;
import com.lab.Factory.FactoryDAO;
import com.lab.Factory.MainDAOFactory;
import com.lab.Interfaces.InterfazConsola;
import com.lab.connections.DBConnect;

public class ConsolaAdministrativa {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("    CONSOLA ADMINISTRATIVA");
        System.out.println("=========================================");
        System.out.println("  Modo administrativo - Sin notificaciones");
        System.out.println("=========================================\n");
        
        DBConnect conexion = DBConnect.getInstancia();
        FactoryDAO factory = MainDAOFactory.getFactory(conexion);
        
        InterfazConsola consolaAdmin = new InterfazConsola(
            conexion, 
            factory.createEstudianteDAO(), 
            factory.createCursoDAO()
        );
        
        consolaAdmin.run();
    }
}