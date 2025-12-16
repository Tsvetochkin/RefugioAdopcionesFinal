package com.refugio.ui;

import com.refugio.controller.AdoptanteController;
import com.refugio.controller.EmpleadoController;
import com.refugio.controller.MascotaController;
import com.refugio.model.adopcion.Adopcion;
import com.refugio.model.persona.Adoptante;
import com.refugio.model.persona.Empleado;
import com.refugio.model.mascota.Mascota;
import com.refugio.servicio.ServicioAdopciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

@Component
public class AdopcionForm extends JFrame {

    @Autowired
    private ServicioAdopciones servicioAdopciones;
    @Autowired
    private AdoptanteController adoptanteController;
    @Autowired
    private EmpleadoController empleadoController;
    @Autowired
    private MascotaController mascotaController;

    private JComboBox<Adoptante> comboAdoptante;
    private JComboBox<Empleado> comboEmpleado;
    private JComboBox<Mascota> comboMascota;
    private JTable tablaAdopciones;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private JTextField txtId; // Hidden field to store the ID for updates

    public AdopcionForm() {
        setTitle("Gestión de Adopciones");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // This method must be called after the bean is created and dependencies are injected.
    public void initUI() {
        // Main panel
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel for inputs
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar/Actualizar Adopción"));

        txtId = new JTextField();
        txtId.setVisible(false);

        comboAdoptante = new JComboBox<>();
        comboEmpleado = new JComboBox<>();
        comboMascota = new JComboBox<>();

        panelFormulario.add(new JLabel("Adoptante:"));
        panelFormulario.add(comboAdoptante);
        panelFormulario.add(new JLabel("Empleado:"));
        panelFormulario.add(comboEmpleado);
        panelFormulario.add(new JLabel("Mascota:"));
        panelFormulario.add(comboMascota);
        panelFormulario.add(txtId); // Add hidden ID field

        // Table for displaying adoptions
        String[] columnas = {"ID", "Mascota", "Adoptante", "Empleado", "Fecha"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        tablaAdopciones = new JTable(tableModel);
        tablaAdopciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Buttons panel
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar Formulario");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Add components to main panel
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tablaAdopciones), BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Add listeners
        btnAgregar.addActionListener(e -> agregarAdopcion());
        btnActualizar.addActionListener(e -> actualizarAdopcion());
        btnEliminar.addActionListener(e -> eliminarAdopcion());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaAdopciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaAdopciones.getSelectedRow() != -1) {
                cargarFormularioDesdeTabla();
            }
        });

        cargarCombos();
        cargarTablaAdopciones();
    }

    private void cargarCombos() {
        comboAdoptante.removeAllItems();
        comboEmpleado.removeAllItems();
        comboMascota.removeAllItems();

        adoptanteController.obtenerTodos().forEach(comboAdoptante::addItem);
        empleadoController.getEmpleados().forEach(comboEmpleado::addItem);
        
        // Logic to show only available pets in the dropdown
        List<Mascota> todasLasMascotas = mascotaController.obtenerTodas();
        List<Adopcion> adopcionesActuales = servicioAdopciones.obtenerTodasLasAdopciones();
        List<Mascota> mascotasAdoptadas = adopcionesActuales.stream().map(Adopcion::getMascota).toList();

        todasLasMascotas.stream()
                .filter(m -> !mascotasAdoptadas.contains(m))
                .forEach(comboMascota::addItem);
    }

    private void cargarTablaAdopciones() {
        tableModel.setRowCount(0); // Clear table
        List<Adopcion> adopciones = servicioAdopciones.obtenerTodasLasAdopciones();
        for (Adopcion adopcion : adopciones) {
            Vector<Object> row = new Vector<>();
            row.add(adopcion.getId());
            row.add(adopcion.getMascota() != null ? adopcion.getMascota().getNombre() : "N/A");
            row.add(adopcion.getAdoptante() != null ? adopcion.getAdoptante().getNombre() : "N/A");
            row.add(adopcion.getEmpleado() != null ? adopcion.getEmpleado().getNombre() : "N/A");
            row.add(adopcion.getFecha());
            tableModel.addRow(row);
        }
    }

    private void cargarFormularioDesdeTabla() {
        int selectedRow = tablaAdopciones.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            servicioAdopciones.obtenerAdopcionPorId(id).ifPresent(adopcion -> {
                txtId.setText(adopcion.getId().toString());
                
                // Add the adopted pet to the combo box temporarily to display it
                Mascota mascotaAdoptada = adopcion.getMascota();
                DefaultComboBoxModel<Mascota> model = (DefaultComboBoxModel<Mascota>) comboMascota.getModel();
                if (model.getIndexOf(mascotaAdoptada) == -1) {
                    model.addElement(mascotaAdoptada);
                }
                
                comboMascota.setSelectedItem(mascotaAdoptada);
                comboAdoptante.setSelectedItem(adopcion.getAdoptante());
                comboEmpleado.setSelectedItem(adopcion.getEmpleado());
            });
        }
    }

    private void agregarAdopcion() {
        try {
            Adoptante adoptante = (Adoptante) comboAdoptante.getSelectedItem();
            Empleado empleado = (Empleado) comboEmpleado.getSelectedItem();
            Mascota mascota = (Mascota) comboMascota.getSelectedItem();

            if (adoptante == null || empleado == null || mascota == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar todas las opciones.");
                return;
            }

            Adopcion<?> adopcion = mascota.crearAdopcion(empleado, adoptante);
            servicioAdopciones.registrarAdopcion(adopcion);

            JOptionPane.showMessageDialog(this, "Adopción registrada exitosamente.");
            cargarTablaAdopciones();
            cargarCombos(); // Recargar combos para que la mascota ya no aparezca
            limpiarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar adopción: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void actualizarAdopcion() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una adopción de la tabla para actualizar.");
            return;
        }

        Long id = Long.parseLong(txtId.getText());
        
        servicioAdopciones.obtenerAdopcionPorId(id).ifPresent(adopcion -> {
            Adoptante adoptante = (Adoptante) comboAdoptante.getSelectedItem();
            Empleado empleado = (Empleado) comboEmpleado.getSelectedItem();
            Mascota mascota = (Mascota) comboMascota.getSelectedItem();

            // Use raw type to bypass generic mismatch
            Adopcion rawAdopcion = adopcion;
            rawAdopcion.setAdoptante(adoptante);
            rawAdopcion.setEmpleado(empleado);
            rawAdopcion.setMascota(mascota);

            servicioAdopciones.actualizarAdopcion(rawAdopcion);
            JOptionPane.showMessageDialog(this, "Adopción actualizada exitosamente.");
            cargarTablaAdopciones();
            cargarCombos();
            limpiarFormulario();
        });
    }

    private void eliminarAdopcion() {
        int selectedRow = tablaAdopciones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una adopción de la tabla para eliminar.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar esta adopción?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            servicioAdopciones.eliminarAdopcion(id);
            JOptionPane.showMessageDialog(this, "Adopción eliminada exitosamente.");
            cargarTablaAdopciones();
            cargarCombos();
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        if (comboAdoptante.getItemCount() > 0) comboAdoptante.setSelectedIndex(0);
        if (comboEmpleado.getItemCount() > 0) comboEmpleado.setSelectedIndex(0);
        if (comboMascota.getItemCount() > 0) comboMascota.setSelectedIndex(0);
        tablaAdopciones.clearSelection();
    }
}