package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.CotizacionDetalleCustomDto;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleEditarRequest;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleNuevoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionDetalleResponse;
import com.telefonicatech.cmdbChile.mapper.CotizacionDetalleMapper;
import com.telefonicatech.cmdbChile.model.CotizacionDetalle;
import com.telefonicatech.cmdbChile.repository.cotizaciones.CotizacionDetalleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CotizacionDetalleService {

    private final CotizacionDetalleRepository repository;
    private final CotizacionDetalleMapper mapper;

    public CotizacionDetalleService(CotizacionDetalleRepository repository, CotizacionDetalleMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<CotizacionDetalleResponse> obtenerDetalleCotizacion(UUID idContrato, Pageable pageable){
        // La consulta nativa usa UUID_TO_BIN(:idContrato) sobre un parámetro String,
        // por eso convertimos a String aquí para evitar problemas de binding de tipos.
        Page<CotizacionDetalleCustomDto> cot = repository.findDetalleCotizacion(idContrato != null ? idContrato.toString() : null, pageable);

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
        r.setIdFamilia(dto.getIdFamilia());
        r.setIdTipoMoneda(dto.getIdTipoMoneda());
        return r;
    }

    @Transactional
    public void addNewDetalle(CotizacionDetalleNuevoRequest req) {
        UUID idContrato = req.getIdContrato();
        List<CotizacionDetalle> allForContrato = repository.findByIdContrato(idContrato);

        int latestVersion = allForContrato.stream()
                .map(CotizacionDetalle::getVersionCotizacion)
                .max(Comparator.naturalOrder())
                .orElse(0);

        List<CotizacionDetalle> prevItems = latestVersion == 0
                ? new ArrayList<>()
                : repository.findByIdContratoAndVersionCotizacion(idContrato, latestVersion);

        int newVersion = latestVersion == 0 ? 1 : latestVersion + 1;

        List<CotizacionDetalle> toSave = new ArrayList<>();

        // copiar items anteriores a la nueva versión (si existen)
        if (!prevItems.isEmpty()) {
            List<CotizacionDetalle> copies = prevItems.stream()
                    .map(p -> mapper.copyForNewVersion(p, newVersion))
                    .collect(Collectors.toList());
            toSave.addAll(copies);
        }

        // calcular numItem para el nuevo elemento
        int nextNumItem = prevItems.stream()
                .map(CotizacionDetalle::getNumItem)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        // mapear request a entidad y añadir
        CotizacionDetalle nuevo = mapper.toEntityForNew(req, newVersion, nextNumItem);
        toSave.add(nuevo);

        repository.saveAll(toSave);
    }

    @Transactional
    public void editDetalle(CotizacionDetalleEditarRequest req) {
        UUID idContrato = req.getIdContrato();
        List<CotizacionDetalle> allForContrato = repository.findByIdContrato(idContrato);

        int latestVersion = allForContrato.stream()
                .map(CotizacionDetalle::getVersionCotizacion)
                .max(Comparator.naturalOrder())
                .orElse(0);

        if (latestVersion == 0) {
            throw new IllegalArgumentException("No existen versiones previas para el contrato: " + idContrato);
        }

        List<CotizacionDetalle> prevItems = repository.findByIdContratoAndVersionCotizacion(idContrato, latestVersion);
        int newVersion = latestVersion + 1;

        List<CotizacionDetalle> toSave = new ArrayList<>();

        // copiar todos los items excepto el que viene en el request
        UUID idDetalleToEdit = req.getIdDetalle();
        for (CotizacionDetalle src : prevItems) {
            if (src.getIdDetalle() != null && src.getIdDetalle().equals(idDetalleToEdit)) {
                // omitimos la copia: será reemplazado por el nuevo elemento
                continue;
            }
            toSave.add(mapper.copyForNewVersion(src, newVersion));
        }

        // usar el numItem proporcionado en el request (según requisito)
        Integer numItem = req.getNumItem();
        if (numItem == null) {
            // fallback: mantener el numItem del item original si no se envió
            numItem = prevItems.stream()
                    .filter(p -> p.getIdDetalle() != null && p.getIdDetalle().equals(idDetalleToEdit))
                    .map(CotizacionDetalle::getNumItem)
                    .findFirst()
                    .orElse(
                            prevItems.stream()
                                    .map(CotizacionDetalle::getNumItem)
                                    .max(Comparator.naturalOrder())
                                    .orElse(0) + 1
                    );
        }

        CotizacionDetalle nuevo = mapper.toEntityForEdit(req, newVersion, numItem);
        toSave.add(nuevo);

        repository.saveAll(toSave);
    }
}
