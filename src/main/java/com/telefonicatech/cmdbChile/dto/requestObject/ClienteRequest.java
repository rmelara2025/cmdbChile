package com.telefonicatech.cmdbChile.dto.requestObject;

import lombok.Data;

@Data
public class ClienteRequest {
    private String rutCliente;
    private String nombreCliente;
    private String nombreComercial;
    private String razonSocial;
    private Integer estado;
}
