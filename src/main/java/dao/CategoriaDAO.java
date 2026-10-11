
package dao;

import config.DatabaseConnection;
import modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listarTodos() throws SQLException {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, nombre FROM categorias";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getInt("id"), rs.getString("nombre"));
                categorias.add(categoria);
            }
        }

        return categorias;
    }


    public boolean insertar(Categoria categoria) throws SQLException {

        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setId(rs.getInt(1));
                    }
                }
            }

            return filas > 0;
        }
    }


    public boolean actualizar(Categoria categoria) throws SQLException {

        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            return ps.executeUpdate() > 0;
        }
    }


    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

}
