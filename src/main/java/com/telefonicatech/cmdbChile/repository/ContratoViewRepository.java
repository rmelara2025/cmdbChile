package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.ContratosView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContratoViewRepository extends JpaRepository<ContratosView, String> {
}
