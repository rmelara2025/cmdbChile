package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

@Data
public class ContactoRequest {
    private String rutCliente;
    private String telefono;
    private String nombre;
    private String email;
    private String cargo;
}

