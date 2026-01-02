package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estadocotizacion")
public class EstadoCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoCotizacion")
    private Integer idEstadoCotizacion;

    @Column(name = "nombre", nullable = false, length = 45, unique = true)
    private String nombre;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "descripcion", length = 100)
    private String descripcion;
}
