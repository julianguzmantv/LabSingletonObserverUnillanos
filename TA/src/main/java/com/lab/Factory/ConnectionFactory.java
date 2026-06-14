package com.lab.Factory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.lab.connections.Connect;
import com.lab.connections.ConnectH2DB;
import com.lab.connections.ConnectMysql;
import com.lab.connections.ConnectOracle;

public class ConnectionFactory{
    public static final String config = "DBActive.properties";

    public static Connect crearConexion(){
        Properties properties = new Properties();
        try(InputStream inputStream = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream(config)){
            properties.load(inputStream);
            String database = properties.getProperty("database.type");
            if("h2".equalsIgnoreCase(database)){
                return new ConnectH2DB();
            } else if ("mysql".equalsIgnoreCase(database)){
                return new ConnectMysql();
            } else if ("oracle".equalsIgnoreCase(database)){
                return new ConnectOracle();
            } else {
                throw new IllegalArgumentException("Tipo de base de datos no reconocida o soportada: " + database);
            }
        } catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException("No se pudo leer el archivo de configuracion" + config);
        }
    }
}
