package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByRutCliente(String rutCliente);
}

