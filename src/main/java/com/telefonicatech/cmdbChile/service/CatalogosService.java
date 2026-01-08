package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.responseObject.PeriodicidadResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.ServicioResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.TipoMonedaResponse;
import com.telefonicatech.cmdbChile.model.Periodicidad;
import com.telefonicatech.cmdbChile.repository.catalogos.PeriodicidadRepository;
import com.telefonicatech.cmdbChile.repository.catalogos.ServicioRepository;
import com.telefonicatech.cmdbChile.repository.catalogos.TipoMonedaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogosService {

    private final ServicioRepository servicioRepository;
    private final TipoMonedaRepository tipoMonedaRepository;
    private final PeriodicidadRepository periodicidadRepository;

    public CatalogosService(
            ServicioRepository servicioRepository,
            TipoMonedaRepository tipoMonedaRepository,
            PeriodicidadRepository periodicidadRepository) {
        this.servicioRepository = servicioRepository;
        this.tipoMonedaRepository = tipoMonedaRepository;
        this.periodicidadRepository = periodicidadRepository;
    }

    public List<ServicioResponse> obtenerServicios() {
        List<Object[]> results = servicioRepository.findAllServiciosConFamilia();
        return results.stream().map(row -> {
            ServicioResponse response = new ServicioResponse();
            response.setIdServicio((Integer) row[0]);
            response.setNombre((String) row[1]);
            response.setDescripcion((String) row[2]);
            response.setIdFamilia((Integer) row[3]);
            response.setNombreFamilia((String) row[4]);
            response.setAtributosSchema((String) row[5]); // Puede ser null
            return response;
        }).collect(Collectors.toList());
    }

    public List<TipoMonedaResponse> obtenerMonedas() {
        List<Object[]> results = tipoMonedaRepository.findAllMonedas();
        return results.stream().map(row -> {
            TipoMonedaResponse response = new TipoMonedaResponse();
            response.setIdTipoMoneda((Integer) row[0]);
            response.setNombreTipoMoneda((String) row[1]);
            response.setCodigo((String) row[1]); // Usando nombreTipoMoneda como código
            return response;
        }).collect(Collectors.toList());
    }

    public List<PeriodicidadResponse> obtenerPeriodicidades() {
        List<Periodicidad> periodicidades = periodicidadRepository.findAll();
        return periodicidades.stream().map(p -> {
            PeriodicidadResponse response = new PeriodicidadResponse();
            response.setIdPeriodicidad(p.getIdPeriodicidad());
            response.setNombre(p.getNombre());
            response.setMeses(p.getMeses());
            response.setDescripcion(p.getDescripcion());
            return response;
        }).collect(Collectors.toList());
    }
}
