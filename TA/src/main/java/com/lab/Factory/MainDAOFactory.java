package com.lab.Factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.lab.connections.DBConnect;

public class MainDAOFactory {
    private static final String config = "DBActive.properties";

    public static FactoryDAO getFactory() {
        DBConnect conexionDB = DBConnect.getInstancia();
        return crearFactoryConConexion(conexionDB);
    }
    
    public static FactoryDAO getFactory(DBConnect conexionDB) {
        if (conexionDB != DBConnect.getInstancia()) {
            System.out.println("Advertencia: No se está usando la instancia singleton de DBConnect");
        }
        return crearFactoryConConexion(conexionDB);
    }
    
    private static FactoryDAO crearFactoryConConexion(DBConnect conexionDB) {
        Properties properties = new Properties();
        try(InputStream inputStream = MainDAOFactory.class
                .getClassLoader()
                .getResourceAsStream(config)){
            
            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo de configuración: " + config);
            }
            
            properties.load(inputStream);
            String database = properties.getProperty("database.type");

            if("h2".equalsIgnoreCase(database)){
                return new H2DAO(conexionDB);
            } else if ("oracle".equalsIgnoreCase(database)){
                return new OracleDAO(conexionDB);
            } else if ("mysql".equalsIgnoreCase(database)){
                return new MySQLDAO(conexionDB);
            } else {
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + database);
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo leer el archivo de configuración: " + config);
        }
    }
}