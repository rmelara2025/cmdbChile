package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.TipoMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoMonedaRepository extends JpaRepository<TipoMoneda, Integer> {

    @Query(value = """
            SELECT idTipoMoneda, nombreTipoMoneda
            FROM tipomoneda
            ORDER BY nombreTipoMoneda
            """, nativeQuery = true)
    List<Object[]> findAllMonedas();
}
