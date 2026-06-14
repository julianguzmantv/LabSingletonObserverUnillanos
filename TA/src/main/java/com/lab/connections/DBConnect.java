package com.lab.connections;
import java.sql.SQLException;
import com.lab.Factory.ConnectionFactory;

public class DBConnect {
    private static final DBConnect INSTANCIA = new DBConnect();
    
    private Connect conexion;
    private boolean conexionActiva = false;

    //singleton
    private DBConnect() {
        //evita la instanciacion multiple por reflexión
        if (INSTANCIA != null) {
            throw new IllegalStateException("Ya existe una instancia de DBConnect");
        }
        inicializarConexion();
    }

    public static DBConnect getInstancia() {
        return INSTANCIA;
    }

    private void inicializarConexion() {
        try {
            this.conexion = ConnectionFactory.crearConexion();
            this.conexionActiva = conexion.getConexion() != null && 
                                 !conexion.getConexion().isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la conexión", e);
        }
    }

    public Connect getConexion() {
        if (!conexionActiva) {
            reconectar();
        }
        return conexion;
    }

    private void reconectar() {
        try {
            cerrarConexion();
            this.conexion = ConnectionFactory.crearConexion();
            this.conexionActiva = true;
        } catch (Exception e) {
            throw new RuntimeException("Error al reconectar", e);
        }
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && conexion.getConexion() != null && 
                !conexion.getConexion().isClosed()) {
                conexion.getConexion().close();
                conexionActiva = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConexionActiva() {
        return conexionActiva;
    }
}