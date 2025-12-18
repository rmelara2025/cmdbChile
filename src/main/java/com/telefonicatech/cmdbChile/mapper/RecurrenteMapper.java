package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.RecurrenteResponse;
import com.telefonicatech.cmdbChile.model.DashboardRecurrentesView;
import org.springframework.stereotype.Component;

@Component
public class RecurrenteMapper {
    public RecurrenteResponse toDto(DashboardRecurrentesView c){
        return new RecurrenteResponse(
                //c.getId(),
                c.getNombreTipoMoneda(),
                c.getEstado(),
                c.getTotalRecurrente(),
                c.getCantidadContratos()
        );
    }
}
