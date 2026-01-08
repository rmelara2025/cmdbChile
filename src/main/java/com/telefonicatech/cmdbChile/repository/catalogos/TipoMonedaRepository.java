package com.telefonicatech.cmdbChile.repository.catalogos;

import com.telefonicatech.cmdbChile.model.TipoMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoMonedaRepository extends JpaRepository<TipoMoneda, Integer> {

    //TODO: refactorizar este metodo, porque perfectamente puedo devolver una lista de la entidad y luego hacer un mapper al response
    @Query(value = """
            SELECT idTipoMoneda, nombreTipoMoneda
            FROM tipomoneda
            ORDER BY nombreTipoMoneda
            """, nativeQuery = true)
    List<Object[]> findAllMonedas();
}
