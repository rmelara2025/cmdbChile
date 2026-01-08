package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.responseObject.PeriodicidadResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.ServicioResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.TipoMonedaResponse;
import com.telefonicatech.cmdbChile.service.CatalogosService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/catalogos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CatalogosController {

    private final CatalogosService service;

    public CatalogosController(CatalogosService service) {
        this.service = service;
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioResponse>> obtenerServicios() {
        return ResponseEntity.ok(service.obtenerServicios());
    }

    @GetMapping("/monedas")
    public ResponseEntity<List<TipoMonedaResponse>> obtenerMonedas() {
        return ResponseEntity.ok(service.obtenerMonedas());
    }

    @GetMapping("/periodicidades")
    public ResponseEntity<List<PeriodicidadResponse>> obtenerPeriodicidades() {
        return ResponseEntity.ok(service.obtenerPeriodicidades());
    }
}
