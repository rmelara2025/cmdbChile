package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.requestObject.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ClienteResponse;
import com.telefonicatech.cmdbChile.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public ClienteResponse toResponse(Cliente e) {
        if (e == null) return null;
        ClienteResponse r = new ClienteResponse();
        r.setRutCliente(e.getRutCliente());
        r.setNombreCliente(e.getNombreCliente());
        r.setNombreComercial(e.getNombreComercial());
        r.setRazonSocial(e.getRazonSocial());
        r.setEstado(e.getEstado());
        return r;
    }

    public Cliente toEntityForCreate(ClienteRequest req) {
        Cliente e = new Cliente();
        e.setRutCliente(req.getRutCliente());
        e.setNombreCliente(req.getNombreCliente());
        e.setNombreComercial(req.getNombreComercial());
        e.setRazonSocial(req.getRazonSocial());
        e.setEstado(req.getEstado());
        return e;
    }

    public void updateEntityFromRequest(Cliente existing, ClienteRequest req) {
        // rut no se modifica por path, pero si se permite en request se ignora aquí
        existing.setNombreCliente(req.getNombreCliente());
        existing.setNombreComercial(req.getNombreComercial());
        existing.setRazonSocial(req.getRazonSocial());
        existing.setEstado(req.getEstado());
    }
}
