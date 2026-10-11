
package dao;

import config.DatabaseConnection;
import modelo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public List<Estudiante> listarTodos() throws SQLException {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT id, nombre, rut, correo, curso FROM estudiantes";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("correo"),
                        rs.getString("curso")
                );

                estudiantes.add(estudiante);
            }
        }

        return estudiantes;
    }

    public boolean insertar(Estudiante estudiante) throws SQLException {

        String sql = """
                INSERT INTO estudiantes (nombre, rut, correo, curso)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion =DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCorreo());
            ps.setString(4, estudiante.getCurso());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        estudiante.setId(rs.getInt(1));
                    }
                }
            }

            return filas > 0;
        }
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {

        String sql = """
                UPDATE estudiantes
                SET nombre = ?, rut = ?, correo = ?, curso = ?
                WHERE id = ?
                """;

        try (Connection conexion = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCorreo());
            ps.setString(4, estudiante.getCurso());
            ps.setInt(5, estudiante.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conexion =DatabaseConnection.getInstance().getConnection();PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
