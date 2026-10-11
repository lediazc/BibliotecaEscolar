
package controlador;

import dao.PrestamoDAO;
import modelo.Prestamo;
import java.sql.SQLException;
import java.util.List;

public class ControladorPrestamos  {

    private PrestamoDAO prestamoDAO;

    public ControladorPrestamos() {
        prestamoDAO = new PrestamoDAO();
    }

    public List<Prestamo> listarTodos() throws SQLException {
        return prestamoDAO.listarTodos();
    }

    public synchronized boolean registrarPrestamo(int idEstudiante, int idLibro) throws SQLException {
        return prestamoDAO.registrarPrestamo(idEstudiante, idLibro);
    }

    public synchronized boolean registrarDevolucion(int idPrestamo) throws SQLException {
        return prestamoDAO.registrarDevolucion(idPrestamo);
    }

    public void registrarPrestamoEnSegundoPlano(int idEstudiante, int idLibro) {
        Thread hilo = new Thread(() -> {
            try {
                boolean registrado = registrarPrestamo(idEstudiante, idLibro);
                System.out.println("Préstamo registrado: " + registrado);
            } catch (SQLException e) {
                System.out.println("Error al registrar préstamo: " + e.getMessage());
            }
        });

        hilo.start();
    }
}
