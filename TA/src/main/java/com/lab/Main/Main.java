package com.lab.Main;

import javax.swing.SwingUtilities;
import com.lab.Factory.FactoryDAO;
import com.lab.Factory.MainDAOFactory;
import com.lab.Interfaces.InterfazConsola;
import com.lab.Interfaces.InterfazMain;
import com.lab.connections.DBConnect;

public class Main {
    public static void main(String[] args) {
        
        DBConnect conexion = DBConnect.getInstancia();
        FactoryDAO factory = MainDAOFactory.getFactory(conexion);
        
        System.out.println("🚀 INICIANDO SISTEMA ACADÉMICO");
        System.out.println("===============================");
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("🖼️  Interfaz Gráfica -> Abriendo ventana...");
            new InterfazMain(factory, conexion);
        });
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("📊 Consola de Monitoreo -> Abriendo ventana...");
            new VentanaMonitoreo();
        });
        
        System.out.println("🎯 CONSOLA ADMINISTRATIVA");
        System.out.println("========================");
        System.out.println("🔕 Notificaciones van a la ventana de monitoreo");
        System.out.println("");
        
        InterfazConsola consolaAdmin = new InterfazConsola(
            conexion, 
            factory.createEstudianteDAO(), 
            factory.createCursoDAO()
        );
        
        consolaAdmin.run();
    }
}