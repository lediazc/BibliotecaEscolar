
package vista;

import controlador.ControladorUsuarios;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtRut = new JTextField();;
    private JPasswordField txtContrasena = new JPasswordField();;
    private JButton btnIngresar = new JButton("Ingresar");;
    private ControladorUsuarios controlador;

    public VentanaLogin() {
        controlador = new ControladorUsuarios();

        setTitle("Biblioteca Escolar - Iniciar sesión");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("RUT:"));
        add(txtRut);
        add(new JLabel("Contraseña:"));
        add(txtContrasena);
        add(new JLabel(""));
        add(btnIngresar);

        btnIngresar.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String rut = txtRut.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (rut.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese RUT y contraseña.");
            return;
        }

        try {
            Usuario usuario = controlador.autenticar(rut, contrasena);

            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre());
                new VentanaPrincipal(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "RUT o contraseña incorrectos.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + e.getMessage());
        }
    }
}
