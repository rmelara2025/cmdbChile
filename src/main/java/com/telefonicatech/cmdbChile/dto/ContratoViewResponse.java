package com.telefonicatech.cmdbChile.dto;

import java.math.BigDecimal;
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
