package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.ContratoViewResponse;
import com.telefonicatech.cmdbChile.dto.TotalRecurrenteResponse;
import com.telefonicatech.cmdbChile.model.ContratosView;
import com.telefonicatech.cmdbChile.model.TotalRecurrentesView;
import org.springframework.stereotype.Component;

@Component
public class TotalRecurrenteMapper {
    public TotalRecurrenteResponse toDto(TotalRecurrentesView c){
        return new TotalRecurrenteResponse(
                //c.getId(),
                c.getNombreTipoMoneda(),
                c.getEstado(),
                c.getTotalRecurrente(),
                c.getCantidadContratos()
        );
    }
}
