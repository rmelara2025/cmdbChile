package com.telefonicatech.cmdbChile.dto.requestObject;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar el estado de una cotización
 * También puede incluir información del usuario que realiza el cambio
 */
@Data
public class CotizacionEstadoUpdateRequest {

    @NotNull(message = "El idEstadoCotizacion es requerido")
    private Integer idEstadoCotizacion;

    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    private String comentario;

    @Size(max = 500, message = "El motivo de rechazo no puede exceder 500 caracteres")
    private String motivoRechazo;

    // ID del usuario que realiza la modificación (puede venir del body o del header
    // HTTP)
    @Size(max = 20, message = "El ID del usuario no puede exceder 20 caracteres")
    private String idUsuarioModificacion;
}
