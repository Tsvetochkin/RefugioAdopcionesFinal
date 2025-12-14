package com.refugio.servicio;

import com.refugio.dao.AdopcionDAO;
import com.refugio.model.adopcion.Adopcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioAdopciones {

    @Autowired
    private AdopcionDAO adopcionDAO;

    public void registrarAdopcion(Adopcion adopcion) {
        adopcionDAO.save(adopcion);
    }

    public List<Adopcion> obtenerTodasLasAdopciones() {
        return adopcionDAO.findAll();
    }

    public Optional<Adopcion> obtenerAdopcionPorId(Long id) {
        return adopcionDAO.findById(id);
    }

    public void actualizarAdopcion(Adopcion adopcion) {
        // The save method handles both creation and update.
        // If the Adopcion object has an ID, it will be updated.
        adopcionDAO.save(adopcion);
    }

    public void eliminarAdopcion(Long id) {
        adopcionDAO.deleteById(id);
    }
}