package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.requestObject.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContactoResponse;
import com.telefonicatech.cmdbChile.service.ContactoService;
import com.telefonicatech.cmdbChile.helper.RutUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoService service;

    public ContactoController(ContactoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ContactoResponse> create(@RequestBody ContactoRequest req) {
        ContactoResponse res = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactoResponse> update(@PathVariable Integer id, @RequestBody ContactoRequest req) {
        ContactoResponse res = service.update(id, req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactoResponse> getById(@PathVariable Integer id) {
        ContactoResponse res = service.getById(id);
        return ResponseEntity.ok(res);
    }



    // Endpoint RESTful recomendado: clientes/{rutCliente}/contactos
    // Por simplicidad lo exponemos bajo /api/contactos/cliente/{rut}
    @GetMapping("/cliente/{rut}")
    public ResponseEntity<List<ContactoResponse>> listByRutPath(@PathVariable("rut") String rut) {
        String formatted = RutUtils.formatRut(rut);
        if (!RutUtils.validateRut(formatted)) {
            throw new IllegalArgumentException("RUT inválido: " + rut);
        }
        List<ContactoResponse> res = service.listByRut(formatted);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
