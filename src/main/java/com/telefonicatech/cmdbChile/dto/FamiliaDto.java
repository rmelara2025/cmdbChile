package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamiliaDto {
    private Integer idFamilia;
    private String nombreFamilia;
    private String descripcion;
}

