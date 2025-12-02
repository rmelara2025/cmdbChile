package com.telefonicatech.cmdbChile.dto;

import java.time.LocalDate;

public record ContratoViewResponse (
        String idContrato,
        String rutCliente,
        String nombreTipoMoneda,
        String nombreTipoPago,
        String nombreCliente,
        LocalDate fechaInicio,
        LocalDate fechaTermino,
        String codSap,
        String codChi,
        String codSison
) {

}
