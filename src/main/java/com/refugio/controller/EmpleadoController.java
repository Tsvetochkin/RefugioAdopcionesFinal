package com.refugio.controller;

import com.refugio.dao.EmpleadoDAO;
import com.refugio.model.persona.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class EmpleadoController {

    @Autowired
    private EmpleadoDAO empleadoDAO;

    public void registrarEmpleado(Empleado empleado) {
        // Здесь можно добавить дополнительную логику, если нужно (например, валидацию)
        empleadoDAO.save(empleado);
    }

    public Optional<Empleado> login(String nombre, String password) {
        // Логика входа
        return empleadoDAO.findByNombreAndPassword(nombre, password);
    }

    public List<Empleado> getEmpleados() {
        return empleadoDAO.findAll();
    }
}