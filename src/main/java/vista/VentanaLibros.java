
package vista;

import controlador.ControladorLibros;
import controlador.ControladorCategorias;
import modelo.Gestionable;
import modelo.Libro;
import modelo.Categoria;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaLibros extends JFrame {

    private Gestionable<Libro> controlador = new ControladorLibros();
    private ControladorCategorias controladorCategorias = new ControladorCategorias();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaLibros = new JTable(modeloTabla);
    private JTextField txtTitulo = new JTextField();
    private JTextField txtAutor = new JTextField();
    private JTextField txtIsbn = new JTextField();
    private JTextField txtEditorial = new JTextField();
    private JTextField txtStock = new JTextField();
    private JComboBox<String> cmbCategorias = new JComboBox<>();
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnVolver = new JButton("Volver");
    private List<Categoria> categorias;
    private Usuario usuario;

    public VentanaLibros(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Biblioteca Escolar - Libros");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"});
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);
        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutor);
        panelFormulario.add(new JLabel("ISBN:"));
        panelFormulario.add(txtIsbn);
        panelFormulario.add(new JLabel("Editorial:"));
        panelFormulario.add(txtEditorial);
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(cmbCategorias);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelFormulario, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        if (!usuario.getRol().equals("bibliotecario")) {
            panelFormulario.setVisible(false);
            btnGuardar.setVisible(false);
            btnActualizar.setVisible(false);
            btnEliminar.setVisible(false);
        }

        cargarCategorias();
        cargarLibros();

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarLibro();
            }
        });

        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnVolver.addActionListener(e -> dispose());
    }

    private void cargarCategorias() {
        try {
            categorias = controladorCategorias.listarTodos();
            cmbCategorias.removeAllItems();

            for (Categoria categoria : categorias) {
                cmbCategorias.addItem(categoria.getNombre());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }

    private void cargarLibros() {
        try {
            modeloTabla.setRowCount(0);

            for (Libro libro : controlador.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getIsbn(),
                        libro.getEditorial(),
                        libro.getStock(),
                        libro.getCategoria().getNombre()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }

    private void seleccionarLibro() {
        int fila = tablaLibros.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtTitulo.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtAutor.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtIsbn.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtEditorial.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtStock.setText(modeloTabla.getValueAt(fila, 5).toString());
        cmbCategorias.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
    }

    private Libro obtenerLibroFormulario() {
        int id = 0;
        int fila = tablaLibros.getSelectedRow();

        if (fila != -1) {
            id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        }

        if (txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Complete título, autor e ISBN.");
        }

        int stock = Integer.parseInt(txtStock.getText().trim());

        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        int indiceCategoria = cmbCategorias.getSelectedIndex();

        if (categorias == null || indiceCategoria == -1) {
            throw new IllegalArgumentException("Seleccione una categoría.");
        }

        Categoria categoria = categorias.get(indiceCategoria);

        return new Libro(id, txtTitulo.getText().trim(), txtAutor.getText().trim(), txtIsbn.getText().trim(), txtEditorial.getText().trim(), stock, categoria);
    }

    private void guardarLibro() {
        if (!usuario.getRol().equals("bibliotecario")) return;

        try {
            Libro libro = obtenerLibroFormulario();
            libro.setId(0);

            if (controlador.insertar(libro)) {
                JOptionPane.showMessageDialog(this, "Libro registrado correctamente.");
                cargarLibros();
                limpiarFormulario();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void actualizarLibro() {
        if (!usuario.getRol().equals("bibliotecario")) return;

        if (tablaLibros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro.");
            return;
        }

        try {
            if (controlador.actualizar(obtenerLibroFormulario())) {
                JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
                cargarLibros();
                limpiarFormulario();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarLibro() {
        if (!usuario.getRol().equals("bibliotecario")) return;

        int fila = tablaLibros.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar el libro seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            if (controlador.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");
                cargarLibros();
                limpiarFormulario();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        tablaLibros.clearSelection();

        if (cmbCategorias.getItemCount() > 0) {
            cmbCategorias.setSelectedIndex(0);
        }
    }
}
