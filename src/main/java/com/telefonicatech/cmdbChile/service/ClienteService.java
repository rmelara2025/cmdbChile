package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.ClienteResponse;
import com.telefonicatech.cmdbChile.helper.RutUtils;
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
        String digitsTerm = null;
        if (term != null) {
            digitsTerm = term.replace(".", "").replace("-", "");
        }
        Page<Cliente> page = repository.searchByTerm(term, digitsTerm, pageable);
        return page.map(mapper::toResponse);
    }

    public ClienteResponse getByRut(String rut) {
        String formatted = RutUtils.formatRut(rut);
        if (!RutUtils.validateRut(formatted)) {
            throw new IllegalArgumentException("RUT inválido: " + rut);
        }
        Cliente c = repository.findById(formatted)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + formatted));
        return mapper.toResponse(c);
    }

    @Transactional
    public ClienteResponse create(ClienteRequest req) {
        if (req.getRutCliente() == null || req.getRutCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("rutCliente es requerido");
        }
        String formatted = RutUtils.formatRut(req.getRutCliente());
        if (!RutUtils.validateRut(formatted)) {
            throw new IllegalArgumentException("RUT inválido: " + req.getRutCliente());
        }
        if (repository.existsById(formatted)) {
            throw new IllegalArgumentException("Cliente ya existe: " + formatted);
        }
        Cliente e = mapper.toEntityForCreate(req);
        Cliente saved = repository.save(e);
        return mapper.toResponse(saved);
    }

    @Transactional
    public ClienteResponse update(String rut, ClienteRequest req) {
        String formattedRut = RutUtils.formatRut(rut);
        if (!RutUtils.validateRut(formattedRut)) {
            throw new IllegalArgumentException("RUT inválido: " + rut);
        }
        Cliente existing = repository.findById(formattedRut)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + formattedRut));
        mapper.updateEntityFromRequest(existing, req);
        Cliente saved = repository.save(existing);
        return mapper.toResponse(saved);
    }
}
