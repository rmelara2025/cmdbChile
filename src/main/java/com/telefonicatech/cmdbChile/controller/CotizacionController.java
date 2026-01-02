package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.CotizacionCompletaResponse;
import com.telefonicatech.cmdbChile.dto.CotizacionResponse;
import com.telefonicatech.cmdbChile.service.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CotizacionController {

    private final CotizacionService service;

    public CotizacionController(CotizacionService service) {
        this.service = service;
    }

    /**
     * Obtiene todas las cotizaciones de un contrato específico
     * GET /api/contratos/{idContrato}/cotizaciones
     */
    @GetMapping("/api/contratos/{idContrato}/cotizaciones")
    public ResponseEntity<List<CotizacionResponse>> obtenerCotizacionesPorContrato(
            @PathVariable String idContrato) {

        // Validar que el idContrato sea un UUID válido
        UUID uuid;
        try {
            uuid = UUID.fromString(idContrato);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idContrato debe ser un UUID válido");
        }

        List<CotizacionResponse> cotizaciones = service.obtenerCotizacionesPorContrato(uuid);
        return ResponseEntity.ok(cotizaciones);
    }

    /**
     * Obtiene el detalle completo de una cotización específica (con items y
     * totales)
     * GET /api/cotizaciones/{idCotizacion}
     */
    @GetMapping("/api/cotizaciones/{idCotizacion}")
    public ResponseEntity<CotizacionCompletaResponse> obtenerCotizacionCompleta(
            @PathVariable String idCotizacion) {

        // Validar que el idCotizacion sea un UUID válido
        UUID uuid;
        try {
            uuid = UUID.fromString(idCotizacion);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idCotizacion debe ser un UUID válido");
        }

        CotizacionCompletaResponse cotizacion = service.obtenerCotizacionCompleta(uuid);
        return ResponseEntity.ok(cotizacion);
    }
}
