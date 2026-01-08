package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.RecurrenteRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.RecurrenteResponse;
import com.telefonicatech.cmdbChile.model.DashboardRecurrentesView;
import com.telefonicatech.cmdbChile.repository.DashboardRecurrenteRepository;
import com.telefonicatech.cmdbChile.repository.RecurrentesCustomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurrentesService {
    private final DashboardRecurrenteRepository repoDashboard;
    private final RecurrentesCustomRepository repoRecurrente;

    public RecurrentesService(DashboardRecurrenteRepository repoDashboard, RecurrentesCustomRepository repoRecurrente){
        this.repoDashboard = repoDashboard;
        this.repoRecurrente = repoRecurrente;
    }

    public List<RecurrenteResponse> getData(){
        return repoDashboard.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RecurrenteResponse> getCustomData(RecurrenteRequest req){
        return repoRecurrente.findCustomTotals(req);
    }



    private RecurrenteResponse mapToResponse(DashboardRecurrentesView e) {

        RecurrenteResponse r = new RecurrenteResponse();

        r.setNombreTipoMoneda(e.getNombreTipoMoneda());
        r.setEstado(e.getEstado());
        r.setTotalRecurrente(e.getTotalRecurrente());
        r.setCantidadContratos(e.getCantidadContratos());

        return r;
    }
}
