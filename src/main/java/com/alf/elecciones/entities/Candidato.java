package com.alf.elecciones.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity; // <--- ESTE IMPORT ES VITAL
import jakarta.persistence.Table;

@Entity
@Table(name = "candidatos") // Plural es estándar en BD
public class Candidato extends PanacheEntity {

    public String nombre;
    public String partido;
    public String fotoUrl;
    public String propuesta;

    public static long totalVotos(Candidato c) {
        // Al ser un método estático de Panache, llamamos a Voto.count
        return Voto.count("candidato", c);
    }
}
