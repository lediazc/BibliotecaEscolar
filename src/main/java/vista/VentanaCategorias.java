
package vista;

import controlador.ControladorCategorias;
import modelo.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import modelo.Gestionable;

public class VentanaCategorias extends JFrame {

    private Gestionable<Categoria> controlador = new ControladorCategorias();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaCategorias = new JTable(modeloTabla);
    private JTextField txtNombre = new JTextField();
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnVolver = new JButton("Volver");

    public VentanaCategorias() {
        setTitle("Biblioteca Escolar - Categorías");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Nombre"});
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaCategorias), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 5, 5));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelFormulario, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        cargarCategorias();

        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarCategoria();
        });

        btnGuardar.addActionListener(e -> guardarCategoria());
        btnActualizar.addActionListener(e -> actualizarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());
        btnVolver.addActionListener(e -> dispose());
    }

    private void cargarCategorias() {
        try {
            modeloTabla.setRowCount(0);

            for (Categoria categoria : controlador.listarTodos()) {
                modeloTabla.addRow(new Object[]{categoria.getId(), categoria.getNombre()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }

    private void seleccionarCategoria() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila != -1) txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
    }

    private void guardarCategoria() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre.");
                return;
            }

            if (controlador.insertar(new Categoria(0, nombre))) {
                JOptionPane.showMessageDialog(this, "Categoría registrada.");
                cargarCategorias();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void actualizarCategoria() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
            return;
        }

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre.");
                return;
            }

            if (controlador.actualizar(new Categoria(id, nombre))) {
                JOptionPane.showMessageDialog(this, "Categoría actualizada.");
                cargarCategorias();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar la categoría seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            if (controlador.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Categoría eliminada.");
                cargarCategorias();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        tablaCategorias.clearSelection();
    }
}
