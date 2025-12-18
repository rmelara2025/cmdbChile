package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.ClienteResponse;
import com.telefonicatech.cmdbChile.mapper.ClienteMapper;
import com.telefonicatech.cmdbChile.model.Cliente;
import com.telefonicatech.cmdbChile.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ClienteResponse> list(String q, Pageable pageable) {
        String term = (q == null || q.trim().isEmpty()) ? null : q.trim();
        Page<Cliente> page = repository.searchByTerm(term, pageable);
        return page.map(mapper::toResponse);
    }

    public ClienteResponse getByRut(String rut) {
        Cliente c = repository.findById(rut)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + rut));
        return mapper.toResponse(c);
    }

    @Transactional
    public ClienteResponse create(ClienteRequest req) {
        if (req.getRutCliente() == null || req.getRutCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("rutCliente es requerido");
        }
        if (repository.existsById(req.getRutCliente())) {
            throw new IllegalArgumentException("Cliente ya existe: " + req.getRutCliente());
        }
        Cliente e = mapper.toEntityForCreate(req);
        Cliente saved = repository.save(e);
        return mapper.toResponse(saved);
    }

    @Transactional
    public ClienteResponse update(String rut, ClienteRequest req) {
        Cliente existing = repository.findById(rut)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + rut));
        mapper.updateEntityFromRequest(existing, req);
        Cliente saved = repository.save(existing);
        return mapper.toResponse(saved);
    }
}
