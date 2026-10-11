package modelo;

import java.sql.SQLException;
import java.util.List;

public interface Gestionable<T> {

    List<T> listarTodos() throws SQLException;

    boolean insertar(T objeto) throws SQLException;

    boolean actualizar(T objeto) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}