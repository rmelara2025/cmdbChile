package com.telefonicatech.cmdbChile.controller.usuarios;

import com.telefonicatech.cmdbChile.dto.requestObject.LoginRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioResponse;
import com.telefonicatech.cmdbChile.service.usuarios.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
public class usuarioController {
    private final UsuarioService usuarioService;

    public usuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/validar")
    public ResponseEntity<UsuarioResponse> validarUsuario(@RequestBody LoginRequest request){
        return ResponseEntity.ok(usuarioService.autenticar(request));
    }
}
