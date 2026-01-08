package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionResponse {

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
}
