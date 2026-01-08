package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactoResponse {
    private Integer idcontacto;
    private String rutCliente;
    private String telefono;
    private String nombre;
    private String email;
    private String cargo;
}

