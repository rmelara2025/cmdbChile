package com.telefonicatech.cmdbChile.dto.requestObject;

import lombok.Data;

@Data
public class ContactoRequest {
    private String rutCliente;
    private String telefono;
    private String nombre;
    private String email;
    private String cargo;
}

