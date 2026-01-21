package com.alf.elecciones.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "estudiantes")
public class Estudiante extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String carnet; // Asegura que no se repita en la DB

    @Column(unique = true, nullable = false, name = "documento_identidad")
    public String documentoIdentidad; // Nuevo campo requerido

    @Column(nullable = false)
    public String nombre;

    @Column(name = "ya_voto")
    public boolean yaVoto = false;

    // Método para buscar por carnet (Panache)
    public static Estudiante findByCarnet(String carnet) {
        return find("carnet", carnet).firstResult();
    }

    // Método para buscar por documento (útil para validaciones)
    public static Estudiante findByDocumento(String documento) {
        return find("documentoIdentidad", documento).firstResult();
    }
}