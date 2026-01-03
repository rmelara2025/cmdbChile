package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

@Data
public class CotizacionVersionResponse {
    private String idNuevaCotizacion;
    private String numeroCotizacion;
    private Integer version;
}
