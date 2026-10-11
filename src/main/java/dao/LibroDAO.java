
package dao;

import config.DatabaseConnection;
import modelo.Categoria;
import modelo.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> listarTodos() throws SQLException {

        List<Libro> libros = new ArrayList<>();

        String sql = """
                SELECT l.id, l.titulo, l.autor, l.isbn,
                       l.editorial, l.stock,
                       c.id AS categoria_id,
                       c.nombre AS categoria_nombre
                FROM libros l
                INNER JOIN categorias c
                    ON l.id_categoria = c.id
                """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Categoria categoria = new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nombre"));

                Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        categoria
                );

                libros.add(libro);
            }
        }

        return libros;
    }


    public boolean insertar(Libro libro) throws SQLException {

        String sql = """
            INSERT INTO libros
            (titulo, autor, isbn, editorial, stock, id_categoria)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getCategoria().getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getInt(1));
                    }
                }
            }

            return filas > 0;
        }
    }


    public boolean actualizar(Libro libro) throws SQLException {

        String sql = """
            UPDATE libros
            SET titulo = ?, autor = ?, isbn = ?,
                editorial = ?, stock = ?, id_categoria = ?
            WHERE id = ?
            """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getCategoria().getId());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;
        }
    }


    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

}
