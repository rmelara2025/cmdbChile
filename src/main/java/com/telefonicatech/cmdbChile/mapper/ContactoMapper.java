package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.ContactoResponse;
import com.telefonicatech.cmdbChile.exception.BadRequestException;
import com.telefonicatech.cmdbChile.model.Contacto;
import com.telefonicatech.cmdbChile.helper.RutUtils;
import org.springframework.stereotype.Component;

@Component
public class ContactoMapper {

    public Contacto toEntity(ContactoRequest req) {
        if (req == null) return null;
        Contacto c = new Contacto();
        if (req.getRutCliente() != null) {
            String formatted = RutUtils.formatRut(req.getRutCliente());
            if (!RutUtils.validateRut(formatted)) {
                throw new BadRequestException("RUT inválido: " + req.getRutCliente());
            }
            c.setRutCliente(formatted);
        }
        c.setTelefono(req.getTelefono());
        c.setNombre(req.getNombre());
        c.setEmail(req.getEmail());
        c.setCargo(req.getCargo());

        // simple email validation if provided
        if (c.getEmail() != null && !c.getEmail().contains("@")) {
            throw new BadRequestException("Email inválido: " + c.getEmail());
        }

        return c;
    }

    public ContactoResponse toResponse(Contacto e) {
        if (e == null) return null;
        return new ContactoResponse(e.getIdcontacto(), e.getRutCliente(), e.getTelefono(), e.getNombre(), e.getEmail(), e.getCargo());
    }
}
