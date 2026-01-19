package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolResponse {
    private Integer idrol;
    private String nombreRol;
    private List<String> permisos;

    // Constructor legacy para compatibilidad
    public UsuarioRolResponse(Integer idrol, String nombreRol) {
        this.idrol = idrol;
        this.nombreRol = nombreRol;
        this.permisos = List.of();
    }
}
