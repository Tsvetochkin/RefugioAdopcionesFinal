package com.refugio.model.mascota;

import com.refugio.model.adopcion.Adopcion;
import com.refugio.model.mascota.estado.EstadoMascota;
import com.refugio.model.persona.Adoptante;
import com.refugio.model.persona.Empleado;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_mascota", discriminatorType = DiscriminatorType.STRING)
public abstract class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String nombre;
    protected String fechaNacimiento;
    protected double peso;

    @Transient
    protected EstadoMascota estado;

    protected final String recomendacionesCuidado;

    public Mascota(String nombre, String fechaNacimiento, double peso, EstadoMascota estado) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.estado = estado;
        this.recomendacionesCuidado = estado.mostrarCuidados(); // фиксируем один раз
    }

    public Mascota() {
        this.recomendacionesCuidado = "";
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public double getPeso() {
        return peso;
    }

    public EstadoMascota getEstado() {
        return estado;
    }

    public void setEstado(EstadoMascota estado) {
        this.estado = estado;
    }

    public boolean quiereJugar() {
        return estado.quiereJugar();
    }

    public String getRecomendacionesCuidado() {
        return recomendacionesCuidado;
    }

    public abstract String getTipo();

    public abstract Adopcion<?> crearAdopcion(Empleado empleado, Adoptante adoptante);

    @Override
    public String toString() {
        return nombre;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return id != null && id.equals(mascota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}