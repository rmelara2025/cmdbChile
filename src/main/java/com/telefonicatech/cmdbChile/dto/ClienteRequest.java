package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

@Data
public class ClienteRequest {
    private String rutCliente;
    private String nombreCliente;
    private String nombreComercial;
    private String razonSocial;
    private Integer estado;
}
