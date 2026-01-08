package com.telefonicatech.cmdbChile.dto.responseObject;

import java.time.LocalDate;

public record ContratoViewResponse (
        String idContrato,
        String rutCliente,

        String nombreCliente,
        LocalDate fechaInicio,
        LocalDate fechaTermino,
        String codSap,
        String codChi,
        String codSison
) {

}
