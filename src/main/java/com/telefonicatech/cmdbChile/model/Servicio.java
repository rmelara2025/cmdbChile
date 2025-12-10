package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "servicio")
public class Servicio {

    @Id
    @Column(name = "idServicio")
    private Integer idServicio;

    @Column(name = "idFamilia")
    private Integer idFamilia;

    @Column(name = "nombreServicio")
    private String nombreServicio;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "atributos", columnDefinition = "json")
    private String atributos;

    @Column(name = "idProveedor")
    private Integer idProveedor;
}

