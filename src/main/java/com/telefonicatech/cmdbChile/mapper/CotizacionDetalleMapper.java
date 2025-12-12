package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.CotizacionDetalleEditarRequest;
import com.telefonicatech.cmdbChile.dto.CotizacionDetalleNuevoRequest;
import com.telefonicatech.cmdbChile.model.CotizacionDetalle;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CotizacionDetalleMapper {
    public CotizacionDetalle toEntityForNew(CotizacionDetalleNuevoRequest req, Integer version, Integer numItem) {
        CotizacionDetalle e = new CotizacionDetalle();
        e.setIdDetalle(UUID.randomUUID()); // autogenerado
        e.setNumItem(numItem);
        e.setVersionCotizacion(version);
        e.setIdContrato(req.getIdContrato());
        e.setIdServicio(req.getIdServicio());
        e.setCantidad(req.getCantidad());
        e.setRecurrente(req.getRecurrente());
        e.setAtributos(req.getAtributos());
        return e;
    }

    public CotizacionDetalle copyForNewVersion(CotizacionDetalle src, Integer newVersion) {
        CotizacionDetalle copy = new CotizacionDetalle();
        copy.setIdDetalle(UUID.randomUUID());
        copy.setNumItem(src.getNumItem());
        copy.setVersionCotizacion(newVersion);
        copy.setIdContrato(src.getIdContrato());
        copy.setIdServicio(src.getIdServicio());
        copy.setCantidad(src.getCantidad());
        copy.setRecurrente(src.getRecurrente());
        copy.setAtributos(src.getAtributos());
        return copy;
    }

    // Mapper para edición: crea una entidad nueva para la nueva versión usando datos del request.
    public CotizacionDetalle toEntityForEdit(CotizacionDetalleEditarRequest req, Integer version, Integer numItem) {
        CotizacionDetalle e = new CotizacionDetalle();
        // no setear idDetalle ni fechaRegistro (serán generados por la BD)
        e.setIdDetalle(UUID.randomUUID());
        e.setNumItem(numItem);
        e.setVersionCotizacion(version);
        e.setIdContrato(req.getIdContrato());
        e.setIdServicio(req.getIdServicio());
        e.setCantidad(req.getCantidad());
        e.setRecurrente(req.getRecurrente());
        e.setAtributos(req.getAtributos());
        return e;
    }
}
