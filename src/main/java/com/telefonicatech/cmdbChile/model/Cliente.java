package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "rutCliente", length = 12)
    private String rutCliente;

    @Column(name = "nombreCliente", length = 255)
    private String nombreCliente;

    @Column(name = "nombreComercial", length = 255)
    private String nombreComercial;

    @Column(name = "razonSocial", length = 255)
    private String razonSocial;

    @Column(name = "estado")
    private Integer estado;
}