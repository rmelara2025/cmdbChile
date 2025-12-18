package com.telefonicatech.cmdbChile.dto;

import java.time.LocalDateTime;

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
    Integer getIdFamilia();
    Integer getIdTipoMoneda();
}