package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.Data;

@Data
public class ClienteResponse {
    private String rutCliente;
    private String nombreCliente;
    private String nombreComercial;
    private String razonSocial;
    private Integer estado;
}
