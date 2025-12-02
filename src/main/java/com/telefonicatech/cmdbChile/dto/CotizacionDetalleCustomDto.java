package com.telefonicatech.cmdbChile.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//@Data
//public class CotizacionDetalleCustomDto {
//    UUID IdDetalle;
//    Integer numItem;
//    Integer versionCotizacion;
//    UUID idContrato;
//    Integer idServicio;
//    Integer cantidad;
//    BigDecimal recurrente;
//    String atributos;
//    LocalDateTime fechaRegistro;
//
//    String nombreServicio;
//    String nombreFamilia;
//    String nombreTipoMoneda;
//}


public interface CotizacionDetalleCustomDto {

    String getIdDetalle();
    Integer getNumItem();
    Integer getVersionCotizacion();
    String getIdContrato();
    Integer getIdServicio();
    Integer getCantidad();
    Double getRecurrente();
    String getAtributos();
    LocalDateTime getFechaRegistro();
    String getNombreServicio();
    String getNombreFamilia();
    String getNombreTipoMoneda();
}