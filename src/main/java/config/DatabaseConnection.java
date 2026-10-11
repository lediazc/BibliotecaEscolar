
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3308/biblioteca";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root123";

    private static DatabaseConnection instancia;

    private DatabaseConnection() {
    }

    // Singleton
    public static synchronized DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    // Obtener conexión JDBC
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
