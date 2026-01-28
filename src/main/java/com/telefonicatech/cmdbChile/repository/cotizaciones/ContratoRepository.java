package com.telefonicatech.cmdbChile.repository.cotizaciones;

import com.telefonicatech.cmdbChile.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, UUID> {
}
