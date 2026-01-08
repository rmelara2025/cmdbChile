package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.requestObject.ClienteFilterRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ClienteResponse;
import com.telefonicatech.cmdbChile.helper.HelperCommons;
import com.telefonicatech.cmdbChile.helper.RutUtils;
import com.telefonicatech.cmdbChile.service.ClienteService;
import com.telefonicatech.cmdbChile.service.ContactoService;
import com.telefonicatech.cmdbChile.dto.responseObject.ContactoResponse;
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
    private final ContactoService contactoService;

    public ClienteController(ClienteService service, HelperCommons helper, ContactoService contactoService) {
        this.service = service;
        this.helpers = helper;
        this.contactoService = contactoService;
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

        String term = null;
        if (filter.getRutCliente() != null && !filter.getRutCliente().trim().isEmpty()) {
            String formatted = RutUtils.formatRut(filter.getRutCliente());
            if (RutUtils.validateRut(formatted)) {
                term = formatted;
            }
        } else if (filter.getNombreCliente() != null && !filter.getNombreCliente().trim().isEmpty()) {
            term = filter.getNombreCliente().trim();
        }

        Page<ClienteResponse> page = service.list(term, pageable);
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

    // Nuevo endpoint: listar contactos de un cliente
    @GetMapping("/{rut}/contactos")
    public ResponseEntity<List<ContactoResponse>> listContactos(@PathVariable("rut") String rut) {
        String formatted = RutUtils.formatRut(rut);
        if (!RutUtils.validateRut(formatted)) {
            throw new IllegalArgumentException("RUT inválido: " + rut);
        }
        List<ContactoResponse> res = contactoService.listByRut(formatted);
        return ResponseEntity.ok(res);
    }
}
