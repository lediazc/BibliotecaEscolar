
package dao;

import config.DatabaseConnection;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class PrestamoDAO {

    public List<Prestamo> listarTodos() throws SQLException {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT p.id, p.fecha_prestamo,
                       p.fecha_devolucion, p.devuelto,
                       e.id AS estudiante_id,
                       e.nombre AS estudiante_nombre,
                       e.rut, e.correo, e.curso,
                       l.id AS libro_id,
                       l.titulo, l.autor, l.isbn,
                       l.editorial, l.stock,
                       c.id AS categoria_id,
                       c.nombre AS categoria_nombre
                FROM prestamos p
                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id
                INNER JOIN libros l
                    ON p.id_libro = l.id
                INNER JOIN categorias c
                    ON l.id_categoria = c.id
                """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();PreparedStatement ps = conexion.prepareStatement(sql);ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getInt("estudiante_id"),
                        rs.getString("estudiante_nombre"),
                        rs.getString("rut"),
                        rs.getString("correo"),
                        rs.getString("curso")
                );

                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("categoria_nombre")
                );

                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        categoria
                );

                LocalDate fechaDevolucion = null;

                if (rs.getDate("fecha_devolucion") != null) {
                    fechaDevolucion = rs.getDate("fecha_devolucion").toLocalDate();
                }

                Prestamo prestamo = new Prestamo(
                        rs.getInt("id"),
                        estudiante,
                        libro,
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        fechaDevolucion,
                        rs.getBoolean("devuelto")
                );

                prestamos.add(prestamo);
            }
        }

        return prestamos;
    }


    public boolean registrarPrestamo(int idEstudiante, int idLibro) throws SQLException {

        String sqlStock = "UPDATE libros SET stock = stock - 1 WHERE id = ? AND stock > 0";
        String sqlPrestamo = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, false)";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection()) {

            conexion.setAutoCommit(false);

            try {
                int filas;

                try (PreparedStatement ps = conexion.prepareStatement(sqlStock)) {
                    ps.setInt(1, idLibro);
                    filas = ps.executeUpdate();
                }

                if (filas == 0) {
                    conexion.rollback();
                    return false;
                }

                try (PreparedStatement ps = conexion.prepareStatement(sqlPrestamo)) {
                    ps.setInt(1, idEstudiante);
                    ps.setInt(2, idLibro);
                    ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    ps.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
                    ps.executeUpdate();
                }

                conexion.commit();
                return true;

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }


    public boolean registrarDevolucion(int idPrestamo) throws SQLException {

        String sqlBuscar = "SELECT id_libro, devuelto FROM prestamos WHERE id = ? FOR UPDATE";
        String sqlDevolucion = "UPDATE prestamos SET devuelto = true WHERE id = ?";
        String sqlStock = "UPDATE libros SET stock = stock + 1 WHERE id = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection()) {

            conexion.setAutoCommit(false);

            try {
                int idLibro;

                try (PreparedStatement ps = conexion.prepareStatement(sqlBuscar)) {
                    ps.setInt(1, idPrestamo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getBoolean("devuelto")) {
                            conexion.rollback();
                            return false;
                        }

                        idLibro = rs.getInt("id_libro");
                    }
                }

                try (PreparedStatement ps = conexion.prepareStatement(sqlDevolucion)) {
                    ps.setInt(1, idPrestamo);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conexion.prepareStatement(sqlStock)) {
                    ps.setInt(1, idLibro);
                    ps.executeUpdate();
                }

                conexion.commit();
                return true;

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

}
