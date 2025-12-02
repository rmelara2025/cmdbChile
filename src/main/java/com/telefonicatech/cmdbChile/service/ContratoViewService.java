package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.ContratoFilterRequest;
import com.telefonicatech.cmdbChile.dto.ContratoViewResponse;
import com.telefonicatech.cmdbChile.mapper.ContratoViewMapper;
import com.telefonicatech.cmdbChile.model.ContratoViewSpecs;
import com.telefonicatech.cmdbChile.model.ContratosView;
import com.telefonicatech.cmdbChile.repository.ContratoViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContratoViewService {
    private final ContratoViewRepository repository;
    private final ContratoViewMapper mapper;

    public ContratoViewService(ContratoViewRepository repository, ContratoViewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ContratoViewResponse> listar(ContratoFilterRequest filter, Pageable pageable) {
        Specification<ContratosView> spec = ContratoViewSpecs.withFilters(filter);
        Page<ContratosView> page = repository.findAll(spec, pageable);
        return mapper.toPageDto(page);
    }
}
