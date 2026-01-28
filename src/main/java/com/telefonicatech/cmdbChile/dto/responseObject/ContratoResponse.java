package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoResponse {

    private UUID idContrato;
    private String rutCliente;
    private String codChi;
    private String codSap;
    private String codSison;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private String observacion;
}
