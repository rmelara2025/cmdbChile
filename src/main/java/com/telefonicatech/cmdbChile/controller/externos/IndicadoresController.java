package com.telefonicatech.cmdbChile.controller.externos;

import com.telefonicatech.cmdbChile.service.externos.IndicadoresService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndicadoresController {
    private final IndicadoresService service;

    public IndicadoresController(IndicadoresService service) {
        this.service = service;
    }

    @GetMapping("/api/indicadores/uf")
    public String uf() {
        return service.obtenerUf();
    }

    @GetMapping("/api/indicadores/dolar")
    public String dolar() {
        return service.obtenerDolar();
    }
}
