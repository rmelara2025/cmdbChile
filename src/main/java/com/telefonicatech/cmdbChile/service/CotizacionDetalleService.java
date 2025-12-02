package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.CotizacionDetalleCustomDto;
import com.telefonicatech.cmdbChile.dto.CotizacionDetalleResponse;
import com.telefonicatech.cmdbChile.repository.CotizacionDetalleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CotizacionDetalleService {

    private final CotizacionDetalleRepository repository;

    public CotizacionDetalleService(CotizacionDetalleRepository repository){
        this.repository = repository;
    }

    public Page<CotizacionDetalleResponse> obtenerDetalleCotizacion(String idContrato, Pageable pageable){
        Page<CotizacionDetalleCustomDto> cot = repository.findDetalleCotizacion(idContrato, pageable);

        return cot.map(this::map);
    }

    private CotizacionDetalleResponse map(CotizacionDetalleCustomDto dto) {
        CotizacionDetalleResponse r = new CotizacionDetalleResponse();

        // Convertir String → UUID
        r.setIdDetalle(dto.getIdDetalle() != null ? UUID.fromString(dto.getIdDetalle()) : null);
        r.setIdContrato(dto.getIdContrato() != null ? UUID.fromString(dto.getIdContrato()) : null);
        r.setNumItem(dto.getNumItem());
        r.setVersionCotizacion(dto.getVersionCotizacion());


        r.setIdServicio(dto.getIdServicio());
        r.setCantidad(dto.getCantidad());
        // Double/String → BigDecimal
        if (dto.getRecurrente() != null) {
            r.setRecurrente(new BigDecimal(dto.getRecurrente().toString()));
        } else {
            r.setRecurrente(null);
        }
        r.setAtributos(dto.getAtributos());
        r.setFechaRegistro(dto.getFechaRegistro());

        r.setNombreServicio(dto.getNombreServicio());
        r.setNombreFamilia(dto.getNombreFamilia());
        r.setNombreTipoMoneda(dto.getNombreTipoMoneda());

        return r;
    }

}
