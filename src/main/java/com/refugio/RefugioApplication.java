package com.refugio;

import com.refugio.dao.AdopcionDAO;
import com.refugio.dao.AdoptanteDAO;
import com.refugio.dao.EmpleadoDAO;
import com.refugio.dao.MascotaDAO;
import com.refugio.model.mascota.*;
import com.refugio.model.mascota.estado.EstadoEnObservacion;
import com.refugio.model.mascota.estado.EstadoMascota;
import com.refugio.model.mascota.estado.EstadoRequiereCuidados;
import com.refugio.model.mascota.estado.EstadoSaludable;
import com.refugio.model.persona.Adoptante;
import com.refugio.model.persona.Empleado;
import com.refugio.ui.LoginUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@SpringBootApplication
public class RefugioApplication implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(RefugioApplication.class.getName());

    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private AdoptanteDAO adoptanteDAO;
    @Autowired
    private MascotaDAO mascotaDAO;
    @Autowired
    private AdopcionDAO adopcionDAO;

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        new SpringApplicationBuilder(RefugioApplication.class)
                .headless(false)
                .run(args);
    }

    @Override
    public void run(String... args) {
        // Data cleanup as requested
        empleadoDAO.findByNombre("q").ifPresent(empleado -> empleadoDAO.delete(empleado));
        empleadoDAO.findByNombre("Dima").ifPresent(empleado -> empleadoDAO.delete(empleado));

        // Initialize with some data
        if (empleadoDAO.count() == 0) {
            Empleado empleado = new Empleado("Noe", 33, "Casa Rosada", "1989-06-15", "password");
            empleadoDAO.save(empleado);
            logger.info("Empleado guardado en base de datos: " + empleado.getNombre());
        }

        if (adoptanteDAO.count() == 0) {
            Adoptante adoptante1 = new Adoptante("Adam", 28, "Palermo Alto 55", "1995-01-01");
            Adoptante adoptante2 = new Adoptante("Eva", 31, "Belgrano R", "1992-03-20");
            adoptanteDAO.save(adoptante1);
            adoptanteDAO.save(adoptante2);
            logger.info("Adoptantes guardados en base de datos.");
        }

        if (mascotaDAO.count() == 0) {
            List<EstadoMascota> estados = Arrays.asList(
                    new EstadoSaludable(),
                    new EstadoRequiereCuidados(),
                    new EstadoEnObservacion()
            );

            List<Mascota> mascotas = new java.util.ArrayList<>(List.of(
                    // Original pets
                    new Kanguro("Saltamontes", "2022-01-01", 200, getRandomEstado(estados)),
                    new Cocodrilo("Bombardiro", "2021-03-14", 100, getRandomEstado(estados)),
                    new Ornitorrinco("Narizon", "2023-05-01", 8, getRandomEstado(estados)),
                    new Gato("Michi", "2020-10-10", 4.4, getRandomEstado(estados)),
                    // 5 new funny pets (final list)
                    new Gato("Gato con Botas", "2019-01-15", 5.2, getRandomEstado(estados)),
                    new Kanguro("Cangri", "2021-11-11", 180, getRandomEstado(estados)),
                    new Cocodrilo("Dandy", "2018-06-01", 220, getRandomEstado(estados)),
                    new Ornitorrinco("Phineas", "2022-05-25", 6.8, getRandomEstado(estados)),
                    new Gato("Garfield", "2020-08-19", 6.1, getRandomEstado(estados))
            ));

            mascotaDAO.saveAll(mascotas);
            logger.info("Mascotas guardadas en base de datos.");
        }


        // Launch the UI
        java.awt.EventQueue.invokeLater(() -> {
            LoginUI loginUI = context.getBean(LoginUI.class);
            loginUI.initUI();
            loginUI.setVisible(true);
        });
    }

    private EstadoMascota getRandomEstado(List<EstadoMascota> estados) {
        return estados.get(new Random().nextInt(estados.size()));
    }
}