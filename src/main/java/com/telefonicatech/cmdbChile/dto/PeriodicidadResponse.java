package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

@Data
public class PeriodicidadResponse {
    private Integer idPeriodicidad;
    private String nombre;
    private Integer meses;
    private String descripcion;
}
