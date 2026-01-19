package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionCreateRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionCompletaResponse;
import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleItemRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionDetalleItemResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionTotalResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.CotizacionVersionResponse;
import com.telefonicatech.cmdbChile.exception.NotFoundException;
import com.telefonicatech.cmdbChile.model.Cotizacion;
import com.telefonicatech.cmdbChile.repository.cotizaciones.CotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CotizacionService {

    private final CotizacionRepository repository;
    private final CodigoGeneradorService codigoGenerador;

    public CotizacionService(CotizacionRepository repository, CodigoGeneradorService codigoGenerador) {
        this.repository = repository;
        this.codigoGenerador = codigoGenerador;
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
        response.setIdCotizacion((String) cotRow[0]);
        response.setNumeroCotizacion((String) cotRow[1]);
        response.setVersion((Integer) cotRow[2]);
        response.setIdestadoCotizacion((Integer) cotRow[3]);
        response.setNombreEstado((String) cotRow[4]);
        response.setFechaCreacion((String) cotRow[5]);
        response.setFechaVigencia((String) cotRow[6]);
        response.setFechaVencimiento((String) cotRow[7]);
        response.setObservacion((String) cotRow[8]);

        // 2. Obtener detalles (items)
        List<Object[]> detallesRows = repository.findDetallesByCotizacion(idCotizacion.toString());
        List<CotizacionDetalleItemResponse> items = new ArrayList<>();

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
            detalle.setNombreMoneda((String) detRow[10]);
            detalle.setIdPeriodicidad((Integer) detRow[11]);
            detalle.setNombrePeriodicidad((String) detRow[12]);
            detalle.setFechaInicioFacturacion((String) detRow[13]);
            detalle.setFechaFinFacturacion((String) detRow[14]);
            detalle.setAtributos((String) detRow[15]);
            detalle.setObservacion((String) detRow[16]);

            items.add(detalle);
        }
        response.setItems(items);

        // 3. Obtener totales por moneda
        List<Object[]> totalesRows = repository.findTotalesByCotizacion(idCotizacion.toString());
        List<CotizacionTotalResponse> totales = new ArrayList<>();

        for (Object[] totRow : totalesRows) {
            CotizacionTotalResponse total = new CotizacionTotalResponse();
            total.setIdTipoMoneda((Integer) totRow[0]);
            total.setNombreMoneda((String) totRow[1]);
            total.setMontoTotal((BigDecimal) totRow[2]);

            totales.add(total);
        }
        response.setTotales(totales);

        return response;
    }

    /**
     * Crea una nueva cotización
     * - Genera automáticamente el código de cotización (COT-YYYY-NNNNNNNN)
     * - Asigna versión 1
     * - Estado inicial BORRADOR (1)
     */
    @Transactional
    public CotizacionResponse crearCotizacion(CotizacionCreateRequest request) {
        System.out.println("Creando nueva cotización para contrato: " + request.getIdContrato());

        // ⭐ GENERAR CÓDIGO DE COTIZACIÓN (Backend)
        String codigoCotizacion = codigoGenerador.generarCodigoCotizacion();
        System.out.println("Código generado: " + codigoCotizacion);

        // Crear nueva cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(UUID.randomUUID());
        cotizacion.setIdContrato(request.getIdContrato());
        cotizacion.setNumeroCotizacion(codigoCotizacion); // ⭐ Backend asigna el código
        cotizacion.setVersion(1); // Primera versión
        cotizacion.setIdEstadoCotizacion(1); // BORRADOR
        cotizacion.setFechaEmision(request.getFechaEmision());
        cotizacion.setFechaVigenciaDesde(request.getFechaVigenciaDesde());
        cotizacion.setFechaVigenciaHasta(request.getFechaVigenciaHasta());
        cotizacion.setObservacion(request.getObservacion());
        cotizacion.setIdUsuarioCreacion(request.getIdUsuarioCreacion());
        cotizacion.setFechaRegistro(LocalDateTime.now());
        cotizacion.setFechaModificacion(LocalDateTime.now());

        // Guardar en base de datos
        try {
            cotizacion = repository.save(cotizacion);
            System.out.println("Cotización creada exitosamente: " + cotizacion.getIdCotizacion());
        } catch (Exception e) {
            System.err.println("Error al guardar cotización: " + e.getMessage());
            throw new RuntimeException("Error al crear cotización: " + e.getMessage(), e);
        }

        // Preparar respuesta
        CotizacionResponse response = new CotizacionResponse();
        response.setIdCotizacion(cotizacion.getIdCotizacion());
        response.setIdContrato(cotizacion.getIdContrato());
        response.setNumeroCotizacion(cotizacion.getNumeroCotizacion());
        response.setVersion(cotizacion.getVersion());
        response.setEstadoNombre("BORRADOR");
        response.setEstadoDescripcion("Cotización en estado borrador");
        response.setObservacion(cotizacion.getObservacion());

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

    /**
     * Versiona una cotización existente (modifica items)
     * - Crea nueva cotización con version+1
     * - ⭐ MANTIENE EL MISMO CÓDIGO (trazabilidad de versiones)
     * - Nueva cotización en estado BORRADOR (1)
     * - Marca anterior como REEMPLAZADA (5)
     */
    @Transactional
    public CotizacionVersionResponse versionarCotizacion(UUID idCotizacionAnterior) {
        System.out.println("Versionando cotización: " + idCotizacionAnterior.toString());

        // Obtener cotizacion anterior
        Cotizacion anterior = repository.findById(idCotizacionAnterior)
                .orElseThrow(() -> {
                    System.out.println("Cotización no encontrada: {}" + idCotizacionAnterior);
                    return new NotFoundException("Cotización no encontrada: " + idCotizacionAnterior);
                });

        System.out.println(String.format("Cotización anterior encontrada - Versión: {}, Contrato: {}",
                anterior.getVersion(), anterior.getIdContrato()));

        // Crear nueva versión
        Cotizacion nueva = new Cotizacion();
        nueva.setIdCotizacion(UUID.randomUUID());
        nueva.setIdContrato(anterior.getIdContrato());

        // ⭐ MANTENER EL MISMO CÓDIGO (no generar nuevo)
        // Al versionar, conservamos el número para trazabilidad
        nueva.setNumeroCotizacion(anterior.getNumeroCotizacion());

        nueva.setVersion(anterior.getVersion() + 1);
        nueva.setIdEstadoCotizacion(1); // BORRADOR
        nueva.setFechaEmision(LocalDate.now());
        nueva.setFechaVigenciaDesde(anterior.getFechaVigenciaDesde());
        nueva.setFechaVigenciaHasta(anterior.getFechaVigenciaHasta());
        nueva.setFechaRegistro(LocalDateTime.now());
        nueva.setIdUsuarioCreacion(anterior.getIdUsuarioCreacion()); // Mantiene el usuario original

        String observacionReemplazo = String.format(
                "Reemplazada por modificación en servicios - Ver v%d",
                nueva.getVersion());
        nueva.setObservacion(observacionReemplazo);

        System.out.println(
                String.format("Guardando nueva versión: {}, v{}, código: {}", 
                    nueva.getIdCotizacion(), nueva.getVersion(), nueva.getNumeroCotizacion()));

        // Guardar nueva versión
        try {
            nueva = repository.save(nueva);
            System.out.println("Nueva versión guardada exitosamente");
        } catch (Exception e) {
            System.out.println("Error al guardar nueva versión: {}" + e.getMessage());
            throw e;
        }

        // Actualizar estado de la anterior a REEMPLAZADA (5 según la base de datos)
        anterior.setIdEstadoCotizacion(5);
        anterior.setObservacion(observacionReemplazo);
        anterior.setFechaModificacion(LocalDateTime.now());

        try {
            repository.save(anterior);
            System.out.println("Cotización anterior marcada como REEMPLAZADA");
        } catch (Exception e) {
            System.out.println("Error al actualizar cotización anterior: {} " + e.getMessage());
            throw e;
        }

        // Preparar respuesta
        CotizacionVersionResponse response = new CotizacionVersionResponse();
        response.setIdNuevaCotizacion(nueva.getIdCotizacion().toString());
        response.setNumeroCotizacion(nueva.getNumeroCotizacion());
        response.setVersion(nueva.getVersion());

        System.out.println(String.format("Versionado completado exitosamente - Nueva ID: {}, v{}",
                response.getIdNuevaCotizacion(), response.getVersion()));

        return response;
    }

    /**
     * Guarda los items de una cotización
     * - Elimina items existentes
     * - Inserta nuevos items
     * - Recalcula totales
     */
    @Transactional
    public void guardarItems(UUID idCotizacion, List<CotizacionDetalleItemRequest> items) {
        // Verificar que la cotización existe
        repository.findById(idCotizacion)
                .orElseThrow(() -> new NotFoundException("Cotización no encontrada: " + idCotizacion));

        // Eliminar items existentes
        repository.deleteDetallesByCotizacion(idCotizacion.toString());

        // Insertar nuevos items
        int numItem = 1;
        for (CotizacionDetalleItemRequest itemReq : items) {
            // No enviamos subtotal porque es una columna generada por la base de datos
            repository.insertDetalle(
                    idCotizacion.toString(),
                    numItem++,
                    itemReq.getIdServicio(),
                    itemReq.getCantidad(),
                    itemReq.getPrecioUnitario(),
                    itemReq.getIdTipoMoneda(),
                    itemReq.getIdPeriodicidad(),
                    itemReq.getFechaInicioFacturacion(),
                    itemReq.getFechaFinFacturacion(),
                    itemReq.getAtributos(),
                    itemReq.getObservacion());
        }

        // Recalcular totales
        recalcularTotales(idCotizacion);
    }

    private void recalcularTotales(UUID idCotizacion) {
        // Eliminar totales anteriores
        repository.deleteTotalesByCotizacion(idCotizacion.toString());

        // Calcular nuevos totales agrupados por moneda
        List<Object[]> totales = repository.calcularTotalesPorMoneda(idCotizacion.toString());

        for (Object[] total : totales) {
            Integer idMoneda = (Integer) total[0];
            BigDecimal monto = (BigDecimal) total[1];

            repository.insertTotal(
                    idCotizacion.toString(),
                    idMoneda,
                    monto);
        }
    }
}
