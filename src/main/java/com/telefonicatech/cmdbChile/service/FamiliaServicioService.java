package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.FamiliaDto;
import com.telefonicatech.cmdbChile.dto.FamiliaServiciosResponse;
import com.telefonicatech.cmdbChile.dto.ServicioDto;
import com.telefonicatech.cmdbChile.model.FamiliaServicio;
import com.telefonicatech.cmdbChile.model.Servicio;
import com.telefonicatech.cmdbChile.repository.FamiliaServicioRepository;
import com.telefonicatech.cmdbChile.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamiliaServicioService {

    private final FamiliaServicioRepository familiaRepo;
    private final ServicioRepository servicioRepo;

    public FamiliaServicioService(FamiliaServicioRepository familiaRepo, ServicioRepository servicioRepo) {
        this.familiaRepo = familiaRepo;
        this.servicioRepo = servicioRepo;
    }

    public FamiliaServiciosResponse obtenerPorFamilia(Integer idFamilia) {
        FamiliaServicio familia = familiaRepo.findById(idFamilia).orElse(null);
        if (familia == null) return null;

        List<Servicio> servicios = servicioRepo.findByIdFamilia(idFamilia);
        List<ServicioDto> servicioDtos = servicios.stream().map(s -> new ServicioDto(
                s.getIdServicio(),
                s.getIdFamilia(),
                s.getNombreServicio(),
                s.getDescripcion(),
                s.getAtributos(),
                s.getIdProveedor()
        )).collect(Collectors.toList());

        return new FamiliaServiciosResponse(familia.getIdFamilia(), familia.getNombreFamilia(), familia.getDescripcion(), servicioDtos);
    }

    public List<FamiliaDto> listarFamilias() {
        List<FamiliaServicio> familias = familiaRepo.findAll();
        return familias.stream().map(f -> new FamiliaDto(f.getIdFamilia(), f.getNombreFamilia(), f.getDescripcion())).collect(Collectors.toList());
    }
}
