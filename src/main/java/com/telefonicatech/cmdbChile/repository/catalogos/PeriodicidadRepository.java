package com.telefonicatech.cmdbChile.repository.catalogos;

import com.telefonicatech.cmdbChile.model.Periodicidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodicidadRepository extends JpaRepository<Periodicidad, Integer> {
}
