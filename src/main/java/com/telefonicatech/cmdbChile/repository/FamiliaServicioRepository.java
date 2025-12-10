package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.FamiliaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaServicioRepository extends JpaRepository<FamiliaServicio, Integer> {
}

