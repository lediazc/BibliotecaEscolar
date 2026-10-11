
package controlador;

import dao.UsuarioDAO;
import modelo.Gestionable;
import modelo.Usuario;
import java.sql.SQLException;
import java.util.List;

public class ControladorUsuarios implements Gestionable<Usuario> {

    private UsuarioDAO usuarioDAO;

    public ControladorUsuarios() {
        usuarioDAO = new UsuarioDAO();
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    public boolean insertar(Usuario usuario) throws SQLException {
        return usuarioDAO.insertar(usuario);
    }

    public boolean actualizar(Usuario usuario) throws SQLException {
        return usuarioDAO.actualizar(usuario);
    }

    public boolean eliminar(int id) throws SQLException {
        return usuarioDAO.eliminar(id);
    }

    public Usuario autenticar(String rut, String contrasena) throws SQLException {
        return usuarioDAO.autenticar(rut, contrasena);
    }
}
