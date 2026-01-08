package com.telefonicatech.cmdbChile.dto.requestObject;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "Debe proporcionar el id de usuario")
    @Valid
    private String idUsuario;

    @NotEmpty(message = "Debe proporcionar la password de usuario")
    @Valid
    private  String clave;
}
