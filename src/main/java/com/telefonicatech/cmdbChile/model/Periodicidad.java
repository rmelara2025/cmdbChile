package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "periodicidad")
public class Periodicidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPeriodicidad")
    private Integer idPeriodicidad;

    @Column(name = "nombre", nullable = false, length = 45, unique = true)
    private String nombre;

    @Column(name = "meses", nullable = false)
    private Integer meses;

    @Column(name = "descripcion", length = 100)
    private String descripcion;
}
