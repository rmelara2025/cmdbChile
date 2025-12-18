package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClienteFilterRequest {
    private Integer page = 0;
    private Integer size = 10;
    private List<String> sort = List.of("rutCliente,desc");

    private String nombreCliente;
}
