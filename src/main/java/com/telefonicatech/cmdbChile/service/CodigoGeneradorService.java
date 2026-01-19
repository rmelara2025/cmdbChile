package com.telefonicatech.cmdbChile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.Year;

/**
 * Servicio para generar códigos únicos de cotización
 * Formato: COT-YYYY-NNNNNNNN (8 dígitos)
 * Ejemplo: COT-2026-00000001
 * 
 * Capacidad: 99,999,999 cotizaciones por año
 * 
 * Maneja concurrencia mediante MAX() con FOR UPDATE en la tabla cotizacion
 * Sin necesidad de tabla adicional de secuencias
 */
@Service
public class CodigoGeneradorService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Genera el siguiente código de cotización disponible para el año actual
     * 
     * @return String código con formato COT-YYYY-NNNNNNNN
     * @throws RuntimeException si hay error al generar el código
     */
    @Transactional
    public String generarCodigoCotizacion() {
        return generarCodigoCotizacion(Year.now().getValue());
    }

    /**
     * Genera el siguiente código de cotización disponible para un año específico
     * 
     * Usa MAX() + FOR UPDATE sobre la tabla cotizacion para garantizar unicidad
     * sin necesidad de tabla auxiliar de secuencias
     * 
     * @param anio Año para el cual generar el código (YYYY)
     * @return String código con formato COT-YYYY-NNNNNNNN
     * @throws RuntimeException si hay error al generar el código
     */
    @Transactional
    public String generarCodigoCotizacion(int anio) {
        try {
            // 1. Obtener el máximo número usado para este año con lock
            // FOR UPDATE bloquea las filas para evitar concurrencia
            String sql = """
                        SELECT COALESCE(
                            MAX(
                                CAST(
                                    SUBSTRING(numeroCotizacion FROM 10) AS UNSIGNED
                                )
                            ),
                            0
                        )
                        FROM cotizacion
                        WHERE numeroCotizacion LIKE :patron
                        FOR UPDATE
                    """;

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("patron", "COT-" + anio + "-%");

            // Obtener el último número usado (o 0 si no hay ninguno)
            Object result = query.getSingleResult();
            Integer ultimoNumero = (result != null) ? ((Number) result).intValue() : 0;

            // 2. Calcular el siguiente número
            Integer siguienteNumero = ultimoNumero + 1;

            // 3. Validar que no se exceda el límite de 8 dígitos (99,999,999)
            if (siguienteNumero > 99999999) {
                throw new RuntimeException(
                        String.format("Se ha excedido el límite de cotizaciones para el año %d (máximo: 99,999,999)",
                                anio));
            }

            // 4. Formatear el código con 8 dígitos: COT-YYYY-NNNNNNNN
            String codigo = String.format("COT-%d-%08d", anio, siguienteNumero);

            System.out.println(String.format("Código generado: %s (año: %d, último número: %d, siguiente: %d)",
                    codigo, anio, ultimoNumero, siguienteNumero));

            return codigo;

        } catch (Exception e) {
            System.err.println("Error al generar código de cotización: " + e.getMessage());
            throw new RuntimeException("Error al generar código de cotización: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el siguiente número disponible sin consumirlo (solo consulta)
     * Útil para preview o validaciones
     * 
     * @param anio Año para consultar
     * @return int siguiente número disponible
     */
    @Transactional(readOnly = true)
    public int obtenerSiguienteNumero(int anio) {
        String sql = """
                    SELECT COALESCE(
                        MAX(
                            CAST(
                                SUBSTRING(numeroCotizacion FROM 10) AS UNSIGNED
                            )
                        ),
                        0
                    )
                    FROM cotizacion
                    WHERE numeroCotizacion LIKE :patron
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("patron", "COT-" + anio + "-%");

        Object result = query.getSingleResult();
        Integer ultimoNumero = (result != null) ? ((Number) result).intValue() : 0;

        return ultimoNumero + 1;
    }

    /**
     * Obtiene estadísticas de códigos generados por año
     * Útil para monitoreo y reporting
     * 
     * @param anio Año para consultar
     * @return int cantidad de códigos generados en el año
     */
    @Transactional(readOnly = true)
    public int contarCotizacionesPorAnio(int anio) {
        String sql = "SELECT COUNT(*) FROM cotizacion WHERE numeroCotizacion LIKE :patron";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("patron", "COT-" + anio + "-%");

        Object result = query.getSingleResult();
        return (result != null) ? ((Number) result).intValue() : 0;
    }
}
