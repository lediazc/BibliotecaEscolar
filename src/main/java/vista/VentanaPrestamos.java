
package vista;

import controlador.ControladorPrestamos;
import controlador.ControladorEstudiantes;
import controlador.ControladorLibros;
import modelo.Prestamo;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Usuario;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrestamos extends JFrame {

    private ControladorPrestamos controlador = new ControladorPrestamos();
    private ControladorEstudiantes controladorEstudiantes = new ControladorEstudiantes();
    private ControladorLibros controladorLibros = new ControladorLibros();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaPrestamos = new JTable(modeloTabla);
    private JComboBox<String> cmbEstudiantes = new JComboBox<>();
    private JComboBox<String> cmbLibros = new JComboBox<>();
    private JButton btnRegistrar = new JButton("Registrar préstamo");
    private JButton btnDevolver = new JButton("Registrar devolución");
    private JButton btnVolver = new JButton("Volver");
    private List<Estudiante> estudiantes;
    private List<Libro> libros;
    private Usuario usuario;

    public VentanaPrestamos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Biblioteca Escolar - Préstamos");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Estudiante", "Libro", "Fecha préstamo", "Fecha devolución", "Devuelto"});
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFormulario.add(new JLabel("Estudiante:"));
        panelFormulario.add(cmbEstudiantes);
        panelFormulario.add(new JLabel("Libro:"));
        panelFormulario.add(cmbLibros);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelFormulario, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();

        btnRegistrar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnVolver.addActionListener(e -> dispose());

        if (!usuario.getRol().equals("bibliotecario")) {
            panelFormulario.setVisible(false);
            btnRegistrar.setVisible(false);
            btnDevolver.setVisible(false);
        }
    }

    private void cargarEstudiantes() {
        try {
            estudiantes = controladorEstudiantes.listarTodos();
            cmbEstudiantes.removeAllItems();

            for (Estudiante estudiante : estudiantes) {
                cmbEstudiantes.addItem(estudiante.getNombre());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + e.getMessage());
        }
    }

    private void cargarLibros() {
        try {
            libros = controladorLibros.listarTodos();
            cmbLibros.removeAllItems();

            for (Libro libro : libros) {
                cmbLibros.addItem(libro.getTitulo() + " | Stock: " + libro.getStock());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }


    private void cargarPrestamos() {
        try {
            modeloTabla.setRowCount(0);

            for (Prestamo prestamo : controlador.listarTodos()) {
                if (!usuario.getRol().equals("bibliotecario") &&
                        !prestamo.getEstudiante().getRut().equals(usuario.getRut())) {
                    continue;
                }

                modeloTabla.addRow(new Object[]{
                        prestamo.getId(),
                        prestamo.getEstudiante().getNombre(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion(),
                        prestamo.isDevuelto()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
        }
    }



    private void registrarPrestamo() {

        if (!usuario.getRol().equals("bibliotecario")) return;

        int indiceEstudiante = cmbEstudiantes.getSelectedIndex();
        int indiceLibro = cmbLibros.getSelectedIndex();

        if (indiceEstudiante == -1 || indiceLibro == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante y un libro.");
            return;
        }

        int idEstudiante = estudiantes.get(indiceEstudiante).getId();
        int idLibro = libros.get(indiceLibro).getId();

        btnRegistrar.setEnabled(false);
        btnDevolver.setEnabled(false);

        Thread hilo = new Thread(() -> {
            try {
                boolean registrado = controlador.registrarPrestamo(idEstudiante, idLibro);

                SwingUtilities.invokeLater(() -> {
                    if (registrado) {
                        JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo registrar el préstamo. Verifique el stock.");
                    }

                    cargarLibros();
                    cargarPrestamos();
                    btnRegistrar.setEnabled(true);
                    btnDevolver.setEnabled(true);
                });

            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + e.getMessage());
                    btnRegistrar.setEnabled(true);
                    btnDevolver.setEnabled(true);
                });
            }
        });

        hilo.start();
    }

    private void registrarDevolucion() {

        if (!usuario.getRol().equals("bibliotecario")) return;

        int fila = tablaPrestamos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo.");
            return;
        }

        int idPrestamo = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Registrar devolución del préstamo " + idPrestamo + "?", "Confirmar devolución", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        btnRegistrar.setEnabled(false);
        btnDevolver.setEnabled(false);

        Thread hilo = new Thread(() -> {
            try {
                boolean devuelto = controlador.registrarDevolucion(idPrestamo);

                SwingUtilities.invokeLater(() -> {
                    if (devuelto) {
                        JOptionPane.showMessageDialog(this, "Devolución registrada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "El préstamo no existe o ya fue devuelto.");
                    }

                    cargarLibros();
                    cargarPrestamos();
                    btnRegistrar.setEnabled(true);
                    btnDevolver.setEnabled(true);
                });

            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error al registrar devolución: " + e.getMessage());
                    btnRegistrar.setEnabled(true);
                    btnDevolver.setEnabled(true);
                });
            }
        });

        hilo.start();
    }

}
