
package vista;

import controlador.ControladorLibros;
import controlador.ControladorPrestamos;
import modelo.Libro;
import modelo.Prestamo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaReportes extends JFrame {

    private ControladorLibros controladorLibros = new ControladorLibros();
    private ControladorPrestamos controladorPrestamos = new ControladorPrestamos();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaReportes = new JTable(modeloTabla);
    private JButton btnInventario = new JButton("Inventario de libros");
    private JButton btnPendientes = new JButton("Préstamos pendientes");
    private JButton btnHistorial = new JButton("Historial de préstamos");
    private JButton btnVolver = new JButton("Volver");

    public VentanaReportes() {
        setTitle("Biblioteca Escolar - Reportes");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnInventario);
        panelBotones.add(btnPendientes);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnVolver);

        add(new JScrollPane(tablaReportes), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnInventario.addActionListener(e -> mostrarInventario());
        btnPendientes.addActionListener(e -> mostrarPendientes());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnVolver.addActionListener(e -> dispose());

        mostrarInventario();
    }

    private void mostrarInventario() {
        try {
            modeloTabla.setRowCount(0);
            modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Título", "Autor", "Categoría", "Stock"});

            for (Libro libro : controladorLibros.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getCategoria().getNombre(),
                        libro.getStock()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage());
        }
    }

    private void mostrarPendientes() {
        try {
            modeloTabla.setRowCount(0);
            modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Estudiante", "Libro", "Fecha préstamo", "Fecha devolución"});

            for (Prestamo prestamo : controladorPrestamos.listarTodos()) {
                if (!prestamo.isDevuelto()) {
                    modeloTabla.addRow(new Object[]{
                            prestamo.getId(),
                            prestamo.getEstudiante().getNombre(),
                            prestamo.getLibro().getTitulo(),
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pendientes: " + e.getMessage());
        }
    }

    private void mostrarHistorial() {
        try {
            modeloTabla.setRowCount(0);
            modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Estudiante", "Libro", "Fecha préstamo", "Fecha devolución", "Devuelto"});

            for (Prestamo prestamo : controladorPrestamos.listarTodos()) {
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
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }
}
