package com.telefonicatech.cmdbChile.dto.requestObject;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para crear una nueva cotización
 * Usado en POST /api/cotizaciones
 */
@Data
public class CotizacionCreateRequest {

    @NotNull(message = "El ID del contrato es obligatorio")
    private UUID idContrato;

    @NotNull(message = "El ID del usuario creador es obligatorio")
    @Size(max = 20, message = "El ID del usuario no puede exceder 20 caracteres")
    private String idUsuarioCreacion;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    private LocalDate fechaVigenciaDesde;

    private LocalDate fechaVigenciaHasta;

    @Size(max = 255, message = "La observación no puede exceder 255 caracteres")
    private String observacion;

    // Nota: numeroCotizacion NO se incluye porque lo genera el backend
    // automáticamente
    // Nota: version siempre es 1 para nuevas cotizaciones
    // Nota: idEstadoCotizacion siempre es 1 (BORRADOR) para nuevas cotizaciones
}
