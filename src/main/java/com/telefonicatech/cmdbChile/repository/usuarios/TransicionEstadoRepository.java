package com.telefonicatech.cmdbChile.repository.usuarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransicionEstadoRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Obtiene las transiciones disponibles para un usuario desde un estado
     * específico
     * 
     * @param idUsuario      ID del usuario
     * @param idEstadoActual ID del estado actual de la cotización
     * @return Lista de transiciones permitidas con información del estado destino
     */
    public List<Map<String, Object>> obtenerAccionesDisponibles(String idUsuario, Integer idEstadoActual) {
        String sql = """
                SELECT DISTINCT
                    te.idTransicion,
                    te.idEstadoDestino,
                    ec.nombre as nombreEstadoDestino,
                    te.descripcion,
                    te.requiereComentario,
                    te.requiereMotivoRechazo,
                    ec.orden
                FROM transicionestado te
                JOIN transicionestadorol ter ON te.idTransicion = ter.idTransicion
                JOIN usuariorol ur ON ter.idRol = ur.idrol
                JOIN estadocotizacion ec ON te.idEstadoDestino = ec.idestadoCotizacion
                WHERE ur.idusuario = :idUsuario
                  AND (te.idEstadoOrigen = :idEstadoActual OR te.idEstadoOrigen IS NULL)
                  AND te.activo = TRUE
                ORDER BY ec.orden
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idEstadoActual", idEstadoActual);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream().map(row -> {
            Map<String, Object> result = new HashMap<>();
            result.put("idTransicion", row[0] != null ? ((Number) row[0]).intValue() : null);
            result.put("idEstadoDestino", row[1] != null ? ((Number) row[1]).intValue() : null);
            result.put("nombreEstadoDestino", row[2] != null ? row[2].toString() : null);
            result.put("descripcion", row[3] != null ? row[3].toString() : null);
            
            // MySQL devuelve TINYINT(1) como Long, necesitamos convertir a Boolean
            result.put("requiereComentario", convertToBoolean(row[4]));
            result.put("requiereMotivoRechazo", convertToBoolean(row[5]));
            // row[6] es ec.orden, no lo necesitamos en el resultado
            return result;
        }).toList();
    }

    /**
     * Convierte un valor que puede ser Boolean o Number a Boolean
     * MySQL devuelve TINYINT(1) como Long en lugar de Boolean
     */
    private boolean convertToBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return false;
    }

    /**
     * Valida si un usuario puede realizar una transición específica
     * 
     * @param idUsuario       ID del usuario
     * @param idEstadoOrigen  ID del estado origen
     * @param idEstadoDestino ID del estado destino
     * @return true si la transición está permitida, false en caso contrario
     */
    public boolean puedeTransicionar(String idUsuario, Integer idEstadoOrigen, Integer idEstadoDestino) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM transicionestado te
                JOIN transicionestadorol ter ON te.idTransicion = ter.idTransicion
                JOIN usuariorol ur ON ter.idRol = ur.idrol
                WHERE ur.idusuario = :idUsuario
                  AND (te.idEstadoOrigen = :idEstadoOrigen OR te.idEstadoOrigen IS NULL)
                  AND te.idEstadoDestino = :idEstadoDestino
                  AND te.activo = TRUE
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idEstadoOrigen", idEstadoOrigen);
        query.setParameter("idEstadoDestino", idEstadoDestino);

        return ((Number) query.getSingleResult()).intValue() > 0;
    }

    /**
     * Valida si la transición requiere comentario obligatorio
     * 
     * @param idEstadoOrigen  ID del estado origen
     * @param idEstadoDestino ID del estado destino
     * @return true si requiere comentario
     */
    public boolean requiereComentario(Integer idEstadoOrigen, Integer idEstadoDestino) {
        String sql = """
                SELECT COALESCE(te.requiereComentario, FALSE)
                FROM transicionestado te
                WHERE (te.idEstadoOrigen = :idEstadoOrigen OR te.idEstadoOrigen IS NULL)
                  AND te.idEstadoDestino = :idEstadoDestino
                  AND te.activo = TRUE
                LIMIT 1
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("idEstadoOrigen", idEstadoOrigen);
        query.setParameter("idEstadoDestino", idEstadoDestino);

        List<?> result = query.getResultList();
        return !result.isEmpty() && convertToBoolean(result.get(0));
    }

    /**
     * Valida si la transición requiere motivo de rechazo obligatorio
     * 
     * @param idEstadoOrigen  ID del estado origen
     * @param idEstadoDestino ID del estado destino
     * @return true si requiere motivo de rechazo
     */
    public boolean requiereMotivoRechazo(Integer idEstadoOrigen, Integer idEstadoDestino) {
        String sql = """
                SELECT COALESCE(te.requiereMotivoRechazo, FALSE)
                FROM transicionestado te
                WHERE (te.idEstadoOrigen = :idEstadoOrigen OR te.idEstadoOrigen IS NULL)
                  AND te.idEstadoDestino = :idEstadoDestino
                  AND te.activo = TRUE
                LIMIT 1
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("idEstadoOrigen", idEstadoOrigen);
        query.setParameter("idEstadoDestino", idEstadoDestino);

        List<?> result = query.getResultList();
        return !result.isEmpty() && convertToBoolean(result.get(0));
    }
}
