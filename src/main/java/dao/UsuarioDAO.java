
package dao;

import config.DatabaseConnection;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id, nombre, rut, correo,
                       `contraseña`, rol
                FROM usuarios
                """;

        try (Connection conexion =DatabaseConnection.getInstance().getConnection();PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol")
                );

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    public boolean insertar(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO usuarios
                (nombre, rut, correo, `contraseña`, rol)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexion =DatabaseConnection.getInstance().getConnection();PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
            }

            return filas > 0;
        }
    }

    public boolean actualizar(Usuario usuario) throws SQLException {

        String sql = """
                UPDATE usuarios
                SET nombre = ?, rut = ?, correo = ?,
                    `contraseña` = ?, rol = ?
                WHERE id = ?
                """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());
            ps.setInt(6, usuario.getId());

            return ps.executeUpdate() > 0;
        }
    }


    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conexion =DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Usuario autenticar(String rut, String contrasena) throws SQLException {

        String sql = """
                SELECT id, nombre, rut, correo,
                       `contraseña`, rol
                FROM usuarios
                WHERE rut = ? AND `contraseña` = ?
                """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, rut);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("correo"),
                            rs.getString("contraseña"),
                            rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }
}
