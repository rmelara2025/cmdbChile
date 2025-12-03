package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.ContratoViewResponse;
import com.telefonicatech.cmdbChile.model.ContratosView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ContratoViewMapper {
    public ContratoViewResponse toDto(ContratosView c){
        return new ContratoViewResponse(
                c.getIdContrato(),
                c.getRutCliente(),
                c.getNombreTipoMoneda(),
                c.getNombreTipoPago(),
                c.getNombreCliente(),
                c.getFechaInicio(),
                c.getFechaTermino(),
                c.getCodSap(),
                c.getCodChi(),
                c.getCodSison()
        );
    }

    public Page<ContratoViewResponse> toPageDto(Page<ContratosView> page) {
        return page.map(this::toDto);
    }
}
