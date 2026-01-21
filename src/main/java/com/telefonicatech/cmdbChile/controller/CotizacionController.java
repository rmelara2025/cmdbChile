package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionCreateRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionCompletaResponse;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionEstadoUpdateRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionItemsUpdateRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionVersionResponse;
import com.telefonicatech.cmdbChile.service.CotizacionService;
import com.telefonicatech.cmdbChile.service.usuarios.TransicionEstadoService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TransicionEstadoService transicionEstadoService;

    public CotizacionController(CotizacionService service, TransicionEstadoService transicionEstadoService) {
        this.service = service;
        this.transicionEstadoService = transicionEstadoService;
    }

    /**
     * Crea una nueva cotización
     * POST /api/cotizaciones
     * El código de cotización (COT-YYYY-NNNNNNNN) es generado automáticamente por
     * el backend
     */
    @PostMapping("/api/cotizaciones")
    public ResponseEntity<CotizacionResponse> crearCotizacion(
            @Valid @RequestBody CotizacionCreateRequest request) {
        try {
            CotizacionResponse response = service.crearCotizacion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear cotización: " + e.getMessage());
        }
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
     * Valida que el usuario tenga permiso para realizar la transición de estado
     */
    @PutMapping("/api/cotizaciones/{idCotizacion}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable String idCotizacion,
            @Valid @RequestBody CotizacionEstadoUpdateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String idUsuario,
            HttpServletRequest httpRequest) {
        try {
            UUID id = UUID.fromString(idCotizacion);

            // Validar que se proporcionó el usuario
            String usuarioFinal = request.getIdUsuarioModificacion() != null
                    ? request.getIdUsuarioModificacion()
                    : idUsuario;

            if (usuarioFinal == null || usuarioFinal.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Se requiere el ID del usuario (header X-User-Id o campo idUsuarioModificacion)");
            }

            // Capturar IP del cliente
            String ipAddress = getClientIpAddress(httpRequest);

            // Actualizar estado con validación de transición
            service.actualizarEstadoConValidacion(
                    id,
                    request.getIdEstadoCotizacion(),
                    usuarioFinal,
                    request.getComentario(),
                    request.getMotivoRechazo(),
                    ipAddress);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al actualizar estado: " + e.getMessage());
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

    /**
     * Obtiene la IP del cliente considerando proxies (X-Forwarded-For)
     * Normaliza IPv6 localhost a IPv4 para mejor legibilidad
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For puede contener múltiples IPs separadas por coma
                // La primera es la IP original del cliente
                ip = ip.split(",")[0].trim();
                return normalizeIpAddress(ip);
            }
        }

        // Fallback a getRemoteAddr()
        String ip = request.getRemoteAddr();
        return normalizeIpAddress(ip != null ? ip : "unknown");
    }

    /**
     * Normaliza direcciones IPv6 localhost a IPv4 para mejor legibilidad
     */
    private String normalizeIpAddress(String ip) {
        // IPv6 localhost: 0:0:0:0:0:0:0:1 o ::1
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }
        return ip;
    }
}
