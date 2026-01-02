package com.telefonicatech.cmdbChile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CotizacionEstadoUpdateRequest {
    @NotNull(message = "El idEstadoCotizacion es requerido")
    private Integer idEstadoCotizacion;
}
