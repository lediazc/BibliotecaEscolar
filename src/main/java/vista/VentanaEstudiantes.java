
package vista;

import controlador.ControladorEstudiantes;
import modelo.Estudiante;
import javax.swing.*;
import modelo.Gestionable;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaEstudiantes extends JFrame {

    private Gestionable<Estudiante> controlador = new ControladorEstudiantes();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaEstudiantes = new JTable(modeloTabla);
    private JTextField txtNombre = new JTextField();
    private JTextField txtRut = new JTextField();
    private JTextField txtCorreo = new JTextField();
    private JTextField txtCurso = new JTextField();
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnVolver = new JButton("Volver");

    public VentanaEstudiantes() {
        setTitle("Biblioteca Escolar - Estudiantes");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Nombre", "RUT", "Correo", "Curso"});
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("RUT:"));
        panelFormulario.add(txtRut);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Curso:"));
        panelFormulario.add(txtCurso);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelFormulario, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        cargarEstudiantes();

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarEstudiante();
        });

        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnActualizar.addActionListener(e -> actualizarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnVolver.addActionListener(e -> dispose());
    }

    private void cargarEstudiantes() {
        try {
            modeloTabla.setRowCount(0);

            for (Estudiante estudiante : controlador.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                        estudiante.getId(),
                        estudiante.getNombre(),
                        estudiante.getRut(),
                        estudiante.getCorreo(),
                        estudiante.getCurso()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + e.getMessage());
        }
    }

    private void seleccionarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) return;

        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtRut.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtCorreo.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtCurso.setText(modeloTabla.getValueAt(fila, 4).toString());
    }

    private void guardarEstudiante() {
        try {
            Estudiante estudiante = obtenerEstudiante(0);

            if (controlador.insertar(estudiante)) {
                JOptionPane.showMessageDialog(this, "Estudiante registrado.");
                cargarEstudiantes();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void actualizarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.");
            return;
        }

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            if (controlador.actualizar(obtenerEstudiante(id))) {
                JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
                cargarEstudiantes();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar el estudiante seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            if (controlador.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Estudiante eliminado.");
                cargarEstudiantes();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private Estudiante obtenerEstudiante(int id) {
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String correo = txtCorreo.getText().trim();
        String curso = txtCurso.getText().trim();

        if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty() || curso.isEmpty()) {
            throw new IllegalArgumentException("Complete todos los campos.");
        }

        return new Estudiante(id, nombre, rut, correo, curso);
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtRut.setText("");
        txtCorreo.setText("");
        txtCurso.setText("");
        tablaEstudiantes.clearSelection();
    }
}
