package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDto {
    private Integer idServicio;
    private Integer idFamilia;
    private String nombreServicio;
    private String descripcion;
    private String atributos;
    private Integer idProveedor;
}

