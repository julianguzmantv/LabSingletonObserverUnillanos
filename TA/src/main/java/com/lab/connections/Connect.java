package com.lab.connections;
import java.sql.Connection;

public interface Connect {
    Connection getConexion();
    String getCurrentDateTimeQuery();
}
