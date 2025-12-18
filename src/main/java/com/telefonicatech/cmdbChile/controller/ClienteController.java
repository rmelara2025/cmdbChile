package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.ClienteFilterRequest;
import com.telefonicatech.cmdbChile.dto.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.ClienteResponse;
import com.telefonicatech.cmdbChile.helper.HelperCommons;
import com.telefonicatech.cmdbChile.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {
    private final ClienteService service;
    private final HelperCommons helpers;

    public ClienteController(ClienteService service, HelperCommons helper) {
        this.service = service;
        this.helpers = helper;
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> list(
            @ModelAttribute ClienteFilterRequest filter) {

        List<Sort.Order> orders = helpers.parseSort(filter.getSort());


        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(orders)
        );

        //return service.listar(filter, pageable);
        Page<ClienteResponse> page = service.list(filter.getNombreCliente(), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<ClienteResponse> getByRut(@PathVariable("rut") String rut) {
        ClienteResponse resp = service.getByRut(rut);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@RequestBody ClienteRequest request) {
        ClienteResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{rut}")
    public ResponseEntity<ClienteResponse> update(@PathVariable("rut") String rut, @RequestBody ClienteRequest request) {
        ClienteResponse updated = service.update(rut, request);
        return ResponseEntity.ok(updated);
    }
}
