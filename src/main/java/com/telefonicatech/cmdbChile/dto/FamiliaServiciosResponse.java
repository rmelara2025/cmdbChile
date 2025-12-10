package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamiliaServiciosResponse {
    private Integer idFamilia;
    private String nombreFamilia;
    private String descripcion;
    private List<ServicioDto> servicios;
}

