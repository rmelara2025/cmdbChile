package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.dto.RecurrenteRequest;
import com.telefonicatech.cmdbChile.dto.RecurrenteResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RecurrentesCustomRepository implements IRecurrentesCustomRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RecurrenteResponse> findCustomTotals(RecurrenteRequest req) {
        // Construimos una subconsulta que calcula estado y los agregados, luego la consulta externa aplica ROW_NUMBER
        StringBuilder sql = new StringBuilder("""
                SELECT   ROW_NUMBER() OVER() AS id,
                    tm.nombreTipoMoneda,
                    CASE
                        WHEN a.fechaTermino < CURDATE() THEN 'expirado'
                        WHEN a.fechaTermino BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 120 DAY) THEN 'por-expirar'
                        ELSE 'vigente'
                    END AS estado,
                    SUM(b.subtotal) AS totalRecurrente,
                    count(distinct a.idContrato) as cantidadContratos
                FROM contratosview a
                JOIN cotizacion c ON a.idContrato = BIN_TO_UUID(c.idContrato)
                AND c.idestadocotizacion = 4
                JOIN cotizaciondetalle b
                        ON BIN_TO_UUID(c.idcotizacion) = BIN_TO_UUID(b.idcotizacion)
                JOIN tipomoneda tm ON tm.idTipoMoneda = b.idTipoMoneda
                WHERE b.idPeriodicidad = 2
               
                """ );

        Map<String, Object> params = new HashMap<>();

        if (req.getRut() != null && !req.getRut().isBlank()) {
            sql.append(" AND a.rutCliente LIKE :rut");
            params.put("rut", "%" + req.getRut().trim() + "%");
        }

        if (req.getNombre() != null && !req.getNombre().isBlank()) {
            sql.append(" AND LOWER(a.nombreCliente) LIKE LOWER(:nombre)");
            params.put("nombre", "%" + req.getNombre().trim() + "%");
        }

        sql.append(" GROUP BY tm.nombreTipoMoneda, estado ");
        sql.append(" ORDER BY tm.nombreTipoMoneda, estado");

        Query query = em.createNativeQuery(sql.toString());

        params.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<RecurrenteResponse> result = new ArrayList<>();

        for (Object[] r : rows) {
            // Column positions: 0=id, 1=nombreTipoMoneda, 2=estado, 3=totalRecurrente, 4=cantidadContratos
            String nombreTipoMoneda = r[1] == null ? null : r[1].toString();
            String estado = r[2] == null ? null : r[2].toString();
            BigDecimal totalRecurrente = null;
            if (r[3] != null) {
                if (r[3] instanceof BigDecimal) totalRecurrente = (BigDecimal) r[3];
                else totalRecurrente = new BigDecimal(r[3].toString());
            }
            Integer cantidadContratos = r[4] == null ? 0 : ((Number) r[4]).intValue();

            result.add(new RecurrenteResponse(
                    nombreTipoMoneda,
                    estado,
                    totalRecurrente,
                    cantidadContratos
            ));
        }

        return result;
    }
}
