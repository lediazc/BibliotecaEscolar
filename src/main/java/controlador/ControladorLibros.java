
package controlador;

import dao.LibroDAO;
import modelo.Gestionable;
import modelo.Libro;
import java.sql.SQLException;
import java.util.List;

public class ControladorLibros implements Gestionable<Libro> {

    private LibroDAO libroDAO;

    public ControladorLibros() {
        libroDAO = new LibroDAO();
    }

    public List<Libro> listarTodos() throws SQLException {
        return libroDAO.listarTodos();
    }

    public boolean insertar(Libro libro) throws SQLException {
        return libroDAO.insertar(libro);
    }

    public boolean actualizar(Libro libro) throws SQLException {
        return libroDAO.actualizar(libro);
    }

    public boolean eliminar(int id) throws SQLException {
        return libroDAO.eliminar(id);
    }
}
