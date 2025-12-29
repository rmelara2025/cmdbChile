package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    @Query("SELECT c FROM Cliente c WHERE (:term IS NULL OR (" +
            "LOWER(c.rutCliente) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(FUNCTION('REPLACE', FUNCTION('REPLACE', c.rutCliente, '.', ''), '-', '')) LIKE LOWER(CONCAT('%', :digitsTerm, '%')) OR " +
            "LOWER(c.nombreCliente) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(c.nombreComercial) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :term, '%'))" +
            "))")
    Page<Cliente> searchByTerm(@Param("term") String term, @Param("digitsTerm") String digitsTerm, Pageable pageable);
}