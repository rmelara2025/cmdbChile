package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.FamiliaDto;
import com.telefonicatech.cmdbChile.dto.FamiliaServiciosResponse;
import com.telefonicatech.cmdbChile.service.FamiliaServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/familias", produces = MediaType.APPLICATION_JSON_VALUE)
public class FamiliaServicioController {

    private final FamiliaServicioService service;

    public FamiliaServicioController(FamiliaServicioService service) {
        this.service = service;
    }

    @GetMapping("/{idFamilia}/servicios")
    public ResponseEntity<FamiliaServiciosResponse> obtenerPorFamilia(@PathVariable Integer idFamilia) {
        FamiliaServiciosResponse res = service.obtenerPorFamilia(idFamilia);
        if (res == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Familia no encontrada");
        return ResponseEntity.ok(res);
    }

    @GetMapping("")
    public ResponseEntity<List<FamiliaDto>> obtenerFamilias() {
        List<FamiliaDto> res = service.listarFamilias();
        return ResponseEntity.ok(res);
    }

}
