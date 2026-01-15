package com.alf.elecciones.entities;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "votos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"estudiante_id"})
})
public class Voto extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "candidato_id") // Define explícitamente el nombre de la FK
    public Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    public Estudiante estudiante;

    // --- Atributos que deberías agregar ---

    // --- ESTE ES EL ATRIBUTO QUE DEBES AGREGAR ---
    @Column(updatable = false)
    public LocalDateTime fecha;

    // Opcional: un constructor o asignar valor por defecto
    public Voto() {
        this.fecha = LocalDateTime.now();
    }

    @Column(length = 50)
    public String ipOrigen; // Útil para auditoría y detectar fraude
}