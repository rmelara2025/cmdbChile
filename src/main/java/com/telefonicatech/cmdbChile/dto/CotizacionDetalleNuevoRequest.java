package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionDetalleNuevoRequest {

    private UUID idContrato;
    private Integer idServicio;
    private Integer cantidad;
    private BigDecimal recurrente;
    private String atributos;

}
