package com.refugio.ui;

import com.refugio.dao.EmpleadoDAO;
import com.refugio.model.persona.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Component
public class LoginUI extends JFrame {

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @Autowired
    private ConfigurableApplicationContext context;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    public void initUI() {
        setLayout(new GridLayout(3, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        JButton registerBtn = new JButton("Register");
        add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegistration());
    }


    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Empleado> empleadoOptional = empleadoDAO.findByNombreAndPassword(username, password);

        if (empleadoOptional.isPresent()) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();

            AdopcionForm adopcionForm = context.getBean(AdopcionForm.class);
            adopcionForm.initUI();
            adopcionForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        dispose();
        RegistroUI registroUI = context.getBean(RegistroUI.class);
        registroUI.initUI();
        registroUI.setVisible(true);
    }
}
