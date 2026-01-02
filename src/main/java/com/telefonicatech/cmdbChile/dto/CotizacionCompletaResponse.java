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
    private UUID idCotizacion;
    private UUID idContrato;
    private String numeroCotizacion;
    private Integer version;
    private String estadoNombre;
    private String estadoDescripcion;
    private String fechaEmision; // formato dd-MM-yyyy
    private String fechaVigenciaDesde; // formato dd-MM-yyyy
    private String fechaVigenciaHasta; // formato dd-MM-yyyy
    private String observacion;
    private String fechaRegistro; // formato dd-MM-yyyy HH:mm:ss

    // Información completa: detalles e items
    private List<CotizacionDetalleItemResponse> detalles;

    // Totales calculados por moneda
    private List<CotizacionTotalResponse> totales;
}
