
package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private Usuario usuario;
    private JLabel lblBienvenida = new JLabel();
    private JButton btnLibros = new JButton("Libros");
    private JButton btnEstudiantes = new JButton("Estudiantes");
    private JButton btnCategorias = new JButton("Categorías");
    private JButton btnPrestamos = new JButton("Préstamos");
    private JButton btnReportes = new JButton("Reportes");
    private JButton btnSalir = new JButton("Cerrar sesión");

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Biblioteca Escolar - Menú principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 10, 10));

        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " | Rol: " + usuario.getRol());

        add(lblBienvenida);
        add(btnLibros);

        if (usuario.getRol().equals("bibliotecario")) {
            add(btnEstudiantes);
            add(btnCategorias);
        }

        add(btnPrestamos);

        if (usuario.getRol().equals("bibliotecario")) {
            add(btnReportes);
        }

        add(btnSalir);

        btnLibros.addActionListener(e -> new VentanaLibros(usuario).setVisible(true));
        btnCategorias.addActionListener(e -> new VentanaCategorias().setVisible(true));
        btnEstudiantes.addActionListener(e -> new VentanaEstudiantes().setVisible(true));
        btnReportes.addActionListener(e -> new VentanaReportes().setVisible(true));
        btnPrestamos.addActionListener(e -> new VentanaPrestamos(usuario).setVisible(true));
        btnSalir.addActionListener(e -> {new VentanaLogin().setVisible(true); dispose();});
    }
}
