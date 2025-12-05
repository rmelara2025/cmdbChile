package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalRecurrenteResponse {
    //private Long id;

    private String nombreTipoMoneda;
    private String estado;
    private BigDecimal totalRecurrente;
    private Integer cantidadContratos;
}
