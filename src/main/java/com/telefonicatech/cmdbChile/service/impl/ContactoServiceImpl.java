package com.telefonicatech.cmdbChile.service.impl;

import com.telefonicatech.cmdbChile.dto.requestObject.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContactoResponse;
import com.telefonicatech.cmdbChile.exception.BadRequestException;
import com.telefonicatech.cmdbChile.exception.NotFoundException;
import com.telefonicatech.cmdbChile.helper.RutUtils;
import com.telefonicatech.cmdbChile.mapper.ContactoMapper;
import com.telefonicatech.cmdbChile.model.Contacto;
import com.telefonicatech.cmdbChile.repository.ClienteRepository;
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
    private final ClienteRepository clienteRepo;

    public ContactoServiceImpl(ContactoRepository repo, ContactoMapper mapper, ClienteRepository clienteRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public ContactoResponse create(ContactoRequest req) {
        if (req.getRutCliente() == null) throw new BadRequestException("rutCliente es requerido");
        String formatted = RutUtils.formatRut(req.getRutCliente());
        if (!RutUtils.validateRut(formatted)) {
            throw new BadRequestException("RUT inválido: " + req.getRutCliente());
        }
        if (!clienteRepo.existsById(formatted)) {
            throw new NotFoundException("Cliente no encontrado: " + formatted);
        }
        Contacto e = mapper.toEntity(req);
        Contacto saved = repo.save(e);
        return mapper.toResponse(saved);
    }

    @Override
    public ContactoResponse update(Integer id, ContactoRequest req) {
        Optional<Contacto> opt = repo.findById(id);
        if (!opt.isPresent()) throw new NotFoundException("Contacto no encontrado: " + id);
        Contacto existing = opt.get();
        // validar cliente
        if (req.getRutCliente() == null) throw new BadRequestException("rutCliente es requerido");
        String formatted = RutUtils.formatRut(req.getRutCliente());
        if (!RutUtils.validateRut(formatted)) {
            throw new BadRequestException("RUT inválido: " + req.getRutCliente());
        }
        if (!clienteRepo.existsById(formatted)) {
            throw new NotFoundException("Cliente no encontrado: " + formatted);
        }
        // map fields from req (keeping idcontacto)
        Contacto updated = mapper.toEntity(req);
        updated.setIdcontacto(existing.getIdcontacto());
        Contacto saved = repo.save(updated);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new NotFoundException("Contacto no encontrado: " + id);
        repo.deleteById(id);
    }

    @Override
    public ContactoResponse getById(Integer id) {
        return repo.findById(id).map(mapper::toResponse).orElseThrow(() -> new NotFoundException("Contacto no encontrado: " + id));
    }

    @Override
    public List<ContactoResponse> listByRut(String rutCliente) {
        return repo.findByRutCliente(rutCliente).stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}
