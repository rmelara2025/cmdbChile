package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.ContratoFilterRequest;
import com.telefonicatech.cmdbChile.dto.CotizacionDetalleResponse;
import com.telefonicatech.cmdbChile.helper.HelperCommons;
import com.telefonicatech.cmdbChile.service.CotizacionDetalleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Page<CotizacionDetalleResponse> obtenerDetalle(@PathVariable String idContrato, @ModelAttribute ContratoFilterRequest filter){

        List<Sort.Order> orders = helpers.parseSort(filter.getSort());


        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(orders)
        );
        return service.obtenerDetalleCotizacion(idContrato,pageable);
    }
}
