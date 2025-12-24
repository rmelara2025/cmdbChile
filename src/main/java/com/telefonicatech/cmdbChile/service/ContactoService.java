package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.ContactoResponse;

import java.util.List;

public interface ContactoService {
    ContactoResponse create(ContactoRequest req);
    ContactoResponse update(Integer id, ContactoRequest req);
    void delete(Integer id);
    ContactoResponse getById(Integer id);
    List<ContactoResponse> listByRut(String rutCliente);
}

