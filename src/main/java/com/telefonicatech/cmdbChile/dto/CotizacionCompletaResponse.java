package com.telefonicatech.cmdbChile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO para consulta de una cotización específica con todos sus detalles.
 * Usado cuando se necesita información completa de UNA cotización.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionCompletaResponse {

    // Datos básicos de la cotización
    private String idCotizacion;
    private String numeroCotizacion;
    private Integer version;
    private Integer idestadoCotizacion;
    private String nombreEstado;
    private String fechaCreacion; // formato dd-MM-yyyy
    private String fechaVigencia; // formato dd-MM-yyyy
    private String fechaVencimiento; // formato dd-MM-yyyy
    private String observacion;

    // Información completa: detalles e items
    private List<CotizacionDetalleItemResponse> items;

    // Totales calculados por moneda
    private List<CotizacionTotalResponse> totales;
}
