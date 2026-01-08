package com.telefonicatech.cmdbChile.dto.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionTotalResponse {

    private Integer idTipoMoneda;
    private String nombreMoneda;
    private BigDecimal montoTotal;
}
