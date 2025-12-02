package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContratoFilterRequest {
    private Integer page = 0;
    private Integer size = 10;
    private List<String> sort = List.of("fechaInicio,desc");

    private String rutCliente;
    private String nombreCliente;
    private String codChi;
    private String codSap;
    private String codSison;
    private String estado;
}
