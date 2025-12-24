package com.telefonicatech.cmdbChile.service.impl;

import com.telefonicatech.cmdbChile.dto.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.ContactoResponse;
import com.telefonicatech.cmdbChile.mapper.ContactoMapper;
import com.telefonicatech.cmdbChile.model.Contacto;
import com.telefonicatech.cmdbChile.repository.ContactoRepository;
import com.telefonicatech.cmdbChile.service.ContactoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactoServiceImpl implements ContactoService {

    private final ContactoRepository repo;
    private final ContactoMapper mapper;

    public ContactoServiceImpl(ContactoRepository repo, ContactoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public ContactoResponse create(ContactoRequest req) {
        Contacto e = mapper.toEntity(req);
        Contacto saved = repo.save(e);
        return mapper.toResponse(saved);
    }

    @Override
    public ContactoResponse update(Integer id, ContactoRequest req) {
        Optional<Contacto> opt = repo.findById(id);
        if (!opt.isPresent()) throw new IllegalArgumentException("Contacto no encontrado: " + id);
        Contacto existing = opt.get();
        // map fields from req (keeping idcontacto)
        Contacto updated = mapper.toEntity(req);
        updated.setIdcontacto(existing.getIdcontacto());
        Contacto saved = repo.save(updated);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Contacto no encontrado: " + id);
        repo.deleteById(id);
    }

    @Override
    public ContactoResponse getById(Integer id) {
        return repo.findById(id).map(mapper::toResponse).orElseThrow(() -> new IllegalArgumentException("Contacto no encontrado: " + id));
    }

    @Override
    public List<ContactoResponse> listByRut(String rutCliente) {
        return repo.findByRutCliente(rutCliente).stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}

