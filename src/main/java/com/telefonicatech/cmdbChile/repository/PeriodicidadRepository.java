package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.Periodicidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodicidadRepository extends JpaRepository<Periodicidad, Integer> {
    List<Periodicidad> findAllByOrderByMesesAsc();
}
