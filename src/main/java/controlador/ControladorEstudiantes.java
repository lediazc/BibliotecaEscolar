
package controlador;

import dao.EstudianteDAO;
import modelo.Estudiante;
import modelo.Gestionable;

import java.sql.SQLException;
import java.util.List;

public class ControladorEstudiantes implements Gestionable<Estudiante> {

    private EstudianteDAO estudianteDAO;

    public ControladorEstudiantes() {
        estudianteDAO = new EstudianteDAO();
    }

    public List<Estudiante> listarTodos() throws SQLException {
        return estudianteDAO.listarTodos();
    }

    public boolean insertar(Estudiante estudiante) throws SQLException {
        return estudianteDAO.insertar(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {
        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminar(int id) throws SQLException {
        return estudianteDAO.eliminar(id);
    }
}
