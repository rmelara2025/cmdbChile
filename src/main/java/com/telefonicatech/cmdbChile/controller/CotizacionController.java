package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionCompletaResponse;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionEstadoUpdateRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionItemsUpdateRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionVersionResponse;
import com.telefonicatech.cmdbChile.service.CotizacionService;
import jakarta.validation.Valid;
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

    /**
     * Actualiza el estado de una cotización
     * PUT /api/cotizaciones/{idCotizacion}/estado
     */
    @PutMapping("/api/cotizaciones/{idCotizacion}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable String idCotizacion,
            @Valid @RequestBody CotizacionEstadoUpdateRequest request) {
        try {
            UUID id = UUID.fromString(idCotizacion);
            service.actualizarEstado(id, request.getIdEstadoCotizacion());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de cotización inválido");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar estado");
        }
    }

    /**
     * Versiona una cotización existente
     * POST /api/cotizaciones/{idCotizacion}/versionar
     */
    @PostMapping("/api/cotizaciones/{idCotizacion}/versionar")
    public ResponseEntity<CotizacionVersionResponse> versionarCotizacion(
            @PathVariable String idCotizacion) {
        try {
            UUID id = UUID.fromString(idCotizacion);
            CotizacionVersionResponse response = service.versionarCotizacion(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de cotización inválido");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al versionar cotización");
        }
    }

    /**
     * Guarda los items de una cotización
     * PUT /api/cotizaciones/{idCotizacion}/items
     */
    @PutMapping("/api/cotizaciones/{idCotizacion}/items")
    public ResponseEntity<Void> guardarItems(
            @PathVariable String idCotizacion,
            @Valid @RequestBody CotizacionItemsUpdateRequest request) {
        try {
            UUID id = UUID.fromString(idCotizacion);
            service.guardarItems(id, request.getItems());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de cotización inválido");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar items");
        }
    }
}
