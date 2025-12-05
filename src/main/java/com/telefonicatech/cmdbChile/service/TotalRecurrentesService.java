package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.TotalRecurrenteResponse;
import com.telefonicatech.cmdbChile.model.TotalRecurrentesView;
import com.telefonicatech.cmdbChile.repository.TotalRecurrentesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalRecurrentesService {
    private final TotalRecurrentesRepository repository;

    public TotalRecurrentesService(TotalRecurrentesRepository repository){
        this.repository = repository;
    }

    public List<TotalRecurrenteResponse> getData(){
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TotalRecurrenteResponse mapToResponse(TotalRecurrentesView e) {

        TotalRecurrenteResponse r = new TotalRecurrenteResponse();

        r.setNombreTipoMoneda(e.getNombreTipoMoneda());
        r.setEstado(e.getEstado());
        r.setTotalRecurrente(e.getTotalRecurrente());
        r.setCantidadContratos(e.getCantidadContratos());

        return r;
    }
}
