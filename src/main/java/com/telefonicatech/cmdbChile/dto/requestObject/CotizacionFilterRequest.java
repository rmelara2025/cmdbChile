package com.telefonicatech.cmdbChile.dto.requestObject;

import lombok.Data;

import java.util.List;

@Data
public class CotizacionFilterRequest {
    private Integer page = 0;
    private Integer size = 10;
    private List<String> sort = List.of("numeroCotizacion,desc");

    private String estado; // Opcional: filtrar por estado
}
