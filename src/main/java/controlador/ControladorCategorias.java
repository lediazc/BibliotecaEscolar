
package controlador;

import dao.CategoriaDAO;
import modelo.Categoria;
import modelo.Gestionable;
import java.sql.SQLException;
import java.util.List;

public class ControladorCategorias implements Gestionable<Categoria> {

    private CategoriaDAO categoriaDAO;

    public ControladorCategorias() {
        categoriaDAO = new CategoriaDAO();
    }

    public List<Categoria> listarTodos() throws SQLException {
        return categoriaDAO.listarTodos();
    }

    public boolean insertar(Categoria categoria) throws SQLException {
        return categoriaDAO.insertar(categoria);
    }

    public boolean actualizar(Categoria categoria) throws SQLException {
        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminar(int id) throws SQLException {
        return categoriaDAO.eliminar(id);
    }
}
