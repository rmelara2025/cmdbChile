package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.Data;

@Data
public class CotizacionVersionResponse {
    private String idNuevaCotizacion;
    private String numeroCotizacion;
    private Integer version;
}
