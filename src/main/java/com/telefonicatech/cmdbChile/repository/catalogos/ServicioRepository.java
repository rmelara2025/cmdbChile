package com.telefonicatech.cmdbChile.repository.catalogos;

import com.telefonicatech.cmdbChile.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByIdFamilia(Integer idFamilia);

    @Query(value = """
            SELECT s.idServicio, s.nombreServicio as nombre, s.descripcion, s.idFamilia,
                   f.nombreFamilia, s.atributos as atributosSchema
            FROM servicio s
            LEFT JOIN familiaservicio f ON s.idFamilia = f.idFamilia
            ORDER BY f.nombreFamilia, s.nombreServicio
            """, nativeQuery = true)
    List<Object[]> findAllServiciosConFamilia();
}
