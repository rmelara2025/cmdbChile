package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionDetalleItemResponse {

    private UUID idDetalle;
    private Integer numItem;

    // Servicio
    private Integer idServicio;
    private String nombreServicio;

    // Familia
    private Integer idFamilia;
    private String nombreFamilia;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Tipo Moneda
    private Integer idTipoMoneda;
    private String nombreMoneda;

    // Periodicidad
    private Integer idPeriodicidad;
    private String nombrePeriodicidad;

    private String fechaInicioFacturacion; // formato dd-MM-yyyy
    private String fechaFinFacturacion; // formato dd-MM-yyyy
    private String atributos; // JSON string
    private String observacion;
}
