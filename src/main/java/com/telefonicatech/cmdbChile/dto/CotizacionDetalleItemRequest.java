package com.telefonicatech.cmdbChile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CotizacionDetalleItemRequest {
    private Integer numItem;

    @NotNull(message = "El servicio es obligatorio")
    private Integer idServicio;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    private BigDecimal precioUnitario;

    @NotNull(message = "La moneda es obligatoria")
    private Integer idTipoMoneda;

    @NotNull(message = "La periodicidad es obligatoria")
    private Integer idPeriodicidad;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaInicioFacturacion;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaFinFacturacion;
    private String atributos; // JSON string
    private String observacion;
}
