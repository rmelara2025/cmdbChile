package com.telefonicatech.cmdbChile.dto.requestObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoRequest {

    @NotBlank(message = "El RUT del cliente es obligatorio")
    private String rutCliente;

    @NotBlank(message = "El tipo de código de proyecto es obligatorio (CHI, SAP o SISON)")
    private String tipoCodigoProyecto; // "CHI", "SAP", "SISON"

    @NotBlank(message = "El código de proyecto es obligatorio")
    private String codigoProyecto; // El valor real: "CHI-2025-001", "SAP-2025-001", etc.

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino;

    private String observacion;
}
