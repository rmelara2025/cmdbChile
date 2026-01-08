package com.telefonicatech.cmdbChile.repository.cotizaciones;

import com.telefonicatech.cmdbChile.model.ContratosView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoViewRepository extends JpaRepository<ContratosView, String>, JpaSpecificationExecutor<ContratosView> {
}
