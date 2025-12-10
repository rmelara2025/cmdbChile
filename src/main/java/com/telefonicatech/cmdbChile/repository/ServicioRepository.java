package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByIdFamilia(Integer idFamilia);
}

