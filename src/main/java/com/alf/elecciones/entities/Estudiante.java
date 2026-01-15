package com.alf.elecciones.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "estudiantes")
public class Estudiante extends PanacheEntity {
    public String carnet;
    public String nombre;
    public boolean yaVoto;

    // Método personalizado para buscar por carnet
    public static Estudiante findByCarnet(String carnet) {
        return find("carnet", carnet).firstResult();
    }
}
