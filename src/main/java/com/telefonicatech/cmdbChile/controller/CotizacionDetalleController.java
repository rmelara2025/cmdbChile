package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.requestObject.ContratoFilterRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleEditarRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleNuevoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionDetalleResponse;
import com.telefonicatech.cmdbChile.helper.HelperCommons;
import com.telefonicatech.cmdbChile.service.CotizacionDetalleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/cotizaciones", produces = MediaType.APPLICATION_JSON_VALUE)
public class CotizacionDetalleController {
    private final CotizacionDetalleService service;
    private final HelperCommons helpers;

    public CotizacionDetalleController(CotizacionDetalleService service, HelperCommons helpers){
        this.service = service;
        this.helpers = helpers;
    }

    @GetMapping("/{idContrato}/detalle")
    public ResponseEntity<Page<CotizacionDetalleResponse>> obtenerDetalle(@PathVariable String idContrato, @ModelAttribute ContratoFilterRequest filter){

        // Validar que el idContrato sea un UUID válido y dar un 400 claro si no lo es
        UUID uuid;
        try {
            uuid = UUID.fromString(idContrato);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idContrato debe ser un UUID válido");
        }

        List<Sort.Order> orders = helpers.parseSort(filter.getSort());


        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(orders)
        );
        Page<CotizacionDetalleResponse> page = service.obtenerDetalleCotizacion(uuid,pageable);
        return ResponseEntity.ok(page);
    }


    @PostMapping
    public ResponseEntity<Void> putNuevoDetalle(@RequestBody CotizacionDetalleNuevoRequest request) {
        service.addNewDetalle(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> putEditarDetalle(@RequestBody CotizacionDetalleEditarRequest request) {
        service.editDetalle(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
