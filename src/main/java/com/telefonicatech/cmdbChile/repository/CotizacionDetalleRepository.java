package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.dto.CotizacionDetalleCustomDto;
import com.telefonicatech.cmdbChile.model.CotizacionDetalle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Repository
public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, UUID> {

    @Query(value = """
        SELECT
            BIN_TO_UUID(a.idDetalle) AS idDetalle,
            a.num_item AS numItem,
            a.version_cotizacion AS versionCotizacion,
            BIN_TO_UUID(a.idContrato) AS idContrato,
            a.idServicio AS idServicio,
            a.cantidad AS cantidad,
            a.recurrente AS recurrente,
            a.atributos AS atributos,
            a.fechaRegistro AS fechaRegistro,
            b.nombreServicio AS nombreServicio,
            c.nombreFamilia AS nombreFamilia,
            e.nombreTipoMoneda AS nombreTipoMoneda,
            b.idFamilia AS idFamilia
        FROM cotizaciondetalle a
        INNER JOIN servicio b ON a.idServicio = b.idServicio
        INNER JOIN familiaservicio c ON b.idFamilia = c.idFamilia
        INNER JOIN contrato d ON d.idContrato = a.idContrato
        INNER JOIN tipomoneda e ON d.idTipoMoneda = e.idTipoMoneda
        WHERE a.idContrato = UUID_TO_BIN(:idContrato)
          AND a.version_cotizacion = (
              SELECT MAX(version_cotizacion)
              FROM cotizaciondetalle
              WHERE idContrato = UUID_TO_BIN(:idContrato)
          )
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM cotizaciondetalle a
        WHERE a.idContrato = UUID_TO_BIN(:idContrato)
          AND a.version_cotizacion = (
              SELECT MAX(version_cotizacion)
              FROM cotizaciondetalle
              WHERE idContrato = UUID_TO_BIN(:idContrato)
          )
        """,
            nativeQuery = true)
    Page<CotizacionDetalleCustomDto> findDetalleCotizacion(@Param("idContrato") String idContrato, Pageable pageable);

    List<CotizacionDetalle> findByIdContrato(UUID idContrato);
    List<CotizacionDetalle> findByIdContratoAndVersionCotizacion(UUID idContrato, Integer versionCotizacion);
}
