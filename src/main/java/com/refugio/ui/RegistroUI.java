package com.refugio.ui;

import com.refugio.controller.EmpleadoController;
import com.refugio.model.persona.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class RegistroUI extends JFrame {

    @Autowired
    private EmpleadoController empleadoController;

    @Autowired
    private ConfigurableApplicationContext context;

    private JTextField nombreField;
    private JTextField edadField;
    private JTextField direccionField;
    private JTextField fechaNacimientoField;
    private JPasswordField passwordField;

    public RegistroUI() {
        setTitle("Ingreso de Empleado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    public void initUI() {
        setLayout(new GridLayout(6, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Edad:"));
        edadField = new JTextField();
        add(edadField);

        add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        add(direccionField);

        add(new JLabel("Fecha Nacimiento (yyyy-MM-dd):"));
        fechaNacimientoField = new JTextField();
        add(fechaNacimientoField);

        JButton guardarBtn = new JButton("Guardar");
        add(guardarBtn);

        JButton cancelarBtn = new JButton("Volver a Login");
        add(cancelarBtn);

        guardarBtn.addActionListener(e -> guardarEmpleado());
        cancelarBtn.addActionListener(e -> volverALogin());
    }

    private void guardarEmpleado() {
        try {
            String nombre = nombreField.getText();
            String password = new String(passwordField.getPassword());

            if (nombre.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre y la contraseña no pueden estar vacíos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int edad = Integer.parseInt(edadField.getText());
            String direccion = direccionField.getText();
            String fechaNacimiento = fechaNacimientoField.getText();

            Empleado nuevoEmpleado = new Empleado(nombre, edad, direccion, fechaNacimiento, password);
            empleadoController.registrarEmpleado(nuevoEmpleado);

            JOptionPane.showMessageDialog(this, "Empleado '" + nombre + "' guardado exitosamente.");
            volverALogin();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverALogin() {
        dispose();
        LoginUI loginUI = context.getBean(LoginUI.class);
        loginUI.initUI();
        loginUI.setVisible(true);
    }
}
