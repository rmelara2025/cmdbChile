package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.CotizacionCompletaResponse;
import com.telefonicatech.cmdbChile.dto.CotizacionDetalleItemResponse;
import com.telefonicatech.cmdbChile.dto.CotizacionResponse;
import com.telefonicatech.cmdbChile.dto.CotizacionTotalResponse;
import com.telefonicatech.cmdbChile.exception.NotFoundException;
import com.telefonicatech.cmdbChile.repository.CotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CotizacionService {

    private final CotizacionRepository repository;

    public CotizacionService(CotizacionRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene todas las cotizaciones de un contrato con joins a estado
     * Las fechas ya vienen formateadas en dd-MM-yyyy desde la query
     */
    public List<CotizacionResponse> obtenerCotizacionesPorContrato(UUID idContrato) {
        List<Object[]> results = repository.findCotizacionesByContrato(idContrato.toString());
        List<CotizacionResponse> responses = new ArrayList<>();

        for (Object[] row : results) {
            CotizacionResponse response = new CotizacionResponse();
            response.setIdCotizacion(UUID.fromString((String) row[0]));
            response.setIdContrato(UUID.fromString((String) row[1]));
            response.setNumeroCotizacion((String) row[2]);
            response.setVersion((Integer) row[3]);
            response.setEstadoNombre((String) row[4]);
            response.setEstadoDescripcion((String) row[5]);
            response.setFechaEmision((String) row[6]);
            response.setFechaVigenciaDesde((String) row[7]);
            response.setFechaVigenciaHasta((String) row[8]);
            response.setObservacion((String) row[9]);
            response.setFechaRegistro((String) row[10]);

            responses.add(response);
        }

        return responses;
    }

    /**
     * Obtiene una cotización específica con todos sus detalles y totales
     */
    public CotizacionCompletaResponse obtenerCotizacionCompleta(UUID idCotizacion) {
        // 1. Obtener datos básicos de la cotización
        List<Object[]> cotRows = repository.findCotizacionById(idCotizacion.toString());

        if (cotRows == null || cotRows.isEmpty()) {
            throw new NotFoundException("Cotización no encontrada con id: " + idCotizacion);
        }

        Object[] cotRow = cotRows.get(0);

        CotizacionCompletaResponse response = new CotizacionCompletaResponse();
        response.setIdCotizacion(UUID.fromString((String) cotRow[0]));
        response.setIdContrato(UUID.fromString((String) cotRow[1]));
        response.setNumeroCotizacion((String) cotRow[2]);
        response.setVersion((Integer) cotRow[3]);
        response.setEstadoNombre((String) cotRow[4]);
        response.setEstadoDescripcion((String) cotRow[5]);
        response.setFechaEmision((String) cotRow[6]);
        response.setFechaVigenciaDesde((String) cotRow[7]);
        response.setFechaVigenciaHasta((String) cotRow[8]);
        response.setObservacion((String) cotRow[9]);
        response.setFechaRegistro((String) cotRow[10]);

        // 2. Obtener detalles (items)
        List<Object[]> detallesRows = repository.findDetallesByCotizacion(idCotizacion.toString());
        List<CotizacionDetalleItemResponse> detalles = new ArrayList<>();

        for (Object[] detRow : detallesRows) {
            CotizacionDetalleItemResponse detalle = new CotizacionDetalleItemResponse();
            detalle.setIdDetalle(UUID.fromString((String) detRow[0]));
            detalle.setNumItem((Integer) detRow[1]);
            detalle.setIdServicio((Integer) detRow[2]);
            detalle.setNombreServicio((String) detRow[3]);
            detalle.setIdFamilia((Integer) detRow[4]);
            detalle.setNombreFamilia((String) detRow[5]);
            detalle.setCantidad((Integer) detRow[6]);
            detalle.setPrecioUnitario((BigDecimal) detRow[7]);
            detalle.setSubtotal((BigDecimal) detRow[8]);
            detalle.setIdTipoMoneda((Integer) detRow[9]);
            detalle.setNombreTipoMoneda((String) detRow[10]);
            detalle.setIdPeriodicidad((Integer) detRow[11]);
            detalle.setPeriodicidad((String) detRow[12]);
            detalle.setFechaInicioFacturacion((String) detRow[13]);
            detalle.setFechaFinFacturacion((String) detRow[14]);
            detalle.setAtributos((String) detRow[15]);
            detalle.setObservacion((String) detRow[16]);

            detalles.add(detalle);
        }
        response.setDetalles(detalles);

        // 3. Obtener totales por moneda
        List<Object[]> totalesRows = repository.findTotalesByCotizacion(idCotizacion.toString());
        List<CotizacionTotalResponse> totales = new ArrayList<>();

        for (Object[] totRow : totalesRows) {
            CotizacionTotalResponse total = new CotizacionTotalResponse();
            total.setNombreMoneda((String) totRow[0]);
            total.setCodigoMoneda((String) totRow[1]);
            total.setTotalOneShot((BigDecimal) totRow[2]);
            total.setTotalMensual((BigDecimal) totRow[3]);
            total.setTotalAnual((BigDecimal) totRow[4]);

            totales.add(total);
        }
        response.setTotales(totales);

        return response;
    }

    /**
     * Actualiza el estado de una cotización
     */
    @Transactional
    public void actualizarEstado(UUID idCotizacion, Integer idEstadoCotizacion) {
        // Verificar que la cotización existe
        repository.findById(idCotizacion)
                .orElseThrow(() -> new NotFoundException("Cotización no encontrada: " + idCotizacion));

        // Actualizar el estado
        repository.updateEstado(idCotizacion.toString(), idEstadoCotizacion);
    }
}
