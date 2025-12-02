package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionDetalleResponse {

    private UUID idDetalle;
    private Integer numItem;
    private Integer versionCotizacion;
    private UUID idContrato;
    private Integer idServicio;
    private Integer cantidad;
    private BigDecimal recurrente;
    private String atributos;
    private LocalDateTime fechaRegistro;

    private String nombreServicio;
    private String nombreFamilia;
    private String nombreTipoMoneda;
}
