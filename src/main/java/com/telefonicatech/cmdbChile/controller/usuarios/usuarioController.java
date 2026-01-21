package com.telefonicatech.cmdbChile.controller.usuarios;

import com.telefonicatech.cmdbChile.dto.requestObject.LoginRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioRolResponse;
import com.telefonicatech.cmdbChile.service.usuarios.TransicionEstadoService;
import com.telefonicatech.cmdbChile.service.usuarios.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
public class usuarioController {
    private final UsuarioService usuarioService;
    private final TransicionEstadoService transicionEstadoService;

    public usuarioController(UsuarioService usuarioService, TransicionEstadoService transicionEstadoService) {
        this.usuarioService = usuarioService;
        this.transicionEstadoService = transicionEstadoService;
    }

    @PostMapping("/validar")
    public ResponseEntity<UsuarioResponse> validarUsuario(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.autenticar(request));
    }

    @GetMapping("/{idusuario}/roles")
    public ResponseEntity<List<UsuarioRolResponse>> getRoles(@PathVariable String idusuario) {
        return ResponseEntity.ok(usuarioService.obtenerRoles(idusuario));
    }

    /**
     * Obtiene las acciones (transiciones de estado) que un usuario puede realizar
     * desde un estado específico de cotización
     * 
     * @param idusuario    ID del usuario
     * @param estadoActual ID del estado actual de la cotización
     * @return Lista de acciones disponibles con información del estado destino
     */
    @GetMapping("/{idusuario}/acciones")
    public ResponseEntity<List<Map<String, Object>>> getAccionesDisponibles(
            @PathVariable String idusuario,
            @RequestParam Integer estadoActual) {
        List<Map<String, Object>> acciones = transicionEstadoService.obtenerAccionesDisponibles(
                idusuario,
                estadoActual);
        return ResponseEntity.ok(acciones);
    }
}
