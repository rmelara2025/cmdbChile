package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, UUID> {

    @Query(value = """
            SELECT
                BIN_TO_UUID(c.`idcotizacion`) AS idCotizacion,
                BIN_TO_UUID(c.`idContrato`) AS idContrato,
                c.`numeroCotizacion` AS numeroCotizacion,
                c.`version` AS version,
                ec.`nombre` AS estadoNombre,
                ec.`descripcion` AS estadoDescripcion,
                DATE_FORMAT(c.`fechaEmision`, '%d-%m-%Y') AS fechaEmision,
                DATE_FORMAT(c.`fechaVigenciaDesde`, '%d-%m-%Y') AS fechaVigenciaDesde,
                DATE_FORMAT(c.`fechaVigenciaHasta`, '%d-%m-%Y') AS fechaVigenciaHasta,
                c.`observacion` AS observacion,
                DATE_FORMAT(c.`fechaRegistro`, '%d-%m-%Y %H:%i:%s') AS fechaRegistro
            FROM `cotizacion` c
            INNER JOIN `estadocotizacion` ec ON c.`idestadoCotizacion` = ec.`idEstadoCotizacion`
            WHERE c.`idContrato` = UUID_TO_BIN(:idContrato)
            ORDER BY c.`numeroCotizacion`, c.`version` DESC
            """, nativeQuery = true)
    List<Object[]> findCotizacionesByContrato(@Param("idContrato") String idContrato);

    @Query(value = """
            SELECT
                BIN_TO_UUID(c.idCotizacion) AS idCotizacion,
                BIN_TO_UUID(c.idContrato) AS idContrato,
                c.numeroCotizacion AS numeroCotizacion,
                c.version AS version,
                ec.nombre AS estadoNombre,
                ec.descripcion AS estadoDescripcion,
                DATE_FORMAT(c.fechaEmision, '%d-%m-%Y') AS fechaEmision,
                DATE_FORMAT(c.fechaVigenciaDesde, '%d-%m-%Y') AS fechaVigenciaDesde,
                DATE_FORMAT(c.fechaVigenciaHasta, '%d-%m-%Y') AS fechaVigenciaHasta,
                c.observacion AS observacion,
                DATE_FORMAT(c.fechaRegistro, '%d-%m-%Y %H:%i:%s') AS fechaRegistro
            FROM cotizacion c
            INNER JOIN estadocotizacion ec ON c.idEstadoCotizacion = ec.idEstadoCotizacion
            WHERE c.idCotizacion = UUID_TO_BIN(:idCotizacion)
            """, nativeQuery = true)
    List<Object[]> findCotizacionById(@Param("idCotizacion") String idCotizacion);

    @Query(value = """
            SELECT
                BIN_TO_UUID(cd.idDetalle) AS idDetalle,
                cd.num_item AS numItem,
                cd.idServicio AS idServicio,
                s.nombreServicio AS nombreServicio,
                fs.idFamilia AS idFamilia,
                fs.nombreFamilia AS nombreFamilia,
                cd.cantidad AS cantidad,
                cd.precioUnitario AS precioUnitario,
                cd.subtotal AS subtotal,
                cd.idTipoMoneda AS idTipoMoneda,
                tm.nombreTipoMoneda AS nombreTipoMoneda,
                cd.idPeriodicidad AS idPeriodicidad,
                p.nombre AS periodicidad,
                DATE_FORMAT(cd.fechaInicioFacturacion, '%d-%m-%Y') AS fechaInicioFacturacion,
                DATE_FORMAT(cd.fechaFinFacturacion, '%d-%m-%Y') AS fechaFinFacturacion,
                cd.atributos AS atributos,
                cd.observacion AS observacion
            FROM cotizaciondetalle cd
            INNER JOIN servicio s ON cd.idServicio = s.idServicio
            INNER JOIN familiaservicio fs ON s.idFamilia = fs.idFamilia
            INNER JOIN tipomoneda tm ON cd.idTipoMoneda = tm.idTipoMoneda
            INNER JOIN periodicidad p ON cd.idPeriodicidad = p.idPeriodicidad
            WHERE cd.idCotizacion = UUID_TO_BIN(:idCotizacion)
            ORDER BY cd.num_item ASC
            """, nativeQuery = true)
    List<Object[]> findDetallesByCotizacion(@Param("idCotizacion") String idCotizacion);

    @Query(value = """
            SELECT
                tm.nombreTipoMoneda AS nombreMoneda,
                tm.nombreTipoMoneda AS codigoMoneda,
                ct.totalOneShot AS totalOneShot,
                ct.totalMensual AS totalMensual,
                ct.totalAnual AS totalAnual
            FROM cotizaciontotal ct
            INNER JOIN tipomoneda tm ON ct.idTipoMoneda = tm.idTipoMoneda
            WHERE ct.idCotizacion = UUID_TO_BIN(:idCotizacion)
            ORDER BY tm.nombreTipoMoneda ASC
            """, nativeQuery = true)
    List<Object[]> findTotalesByCotizacion(@Param("idCotizacion") String idCotizacion);

    @Modifying
    @Query(value = """
            UPDATE cotizacion
            SET idEstadoCotizacion = :idEstadoCotizacion,
                fechaModificacion = NOW()
            WHERE idCotizacion = UUID_TO_BIN(:idCotizacion)
            """, nativeQuery = true)
    void updateEstado(@Param("idCotizacion") String idCotizacion,
            @Param("idEstadoCotizacion") Integer idEstadoCotizacion);
}
