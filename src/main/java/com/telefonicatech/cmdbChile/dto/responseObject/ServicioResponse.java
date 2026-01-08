package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.Data;

@Data
public class ServicioResponse {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private Integer idFamilia;
    private String nombreFamilia;
    private String atributosSchema; // JSON schema para validación en frontend
}
