package com.telefonicatech.cmdbChile.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CotizacionItemsUpdateRequest {
    @NotEmpty(message = "Debe proporcionar al menos un item")
    @Valid
    private List<CotizacionDetalleItemRequest> items;
}
