package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.CotizacionDetalleItemRequest;
import com.telefonicatech.cmdbChile.model.Cotizacion;
import com.telefonicatech.cmdbChile.repository.cotizaciones.CotizacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CotizacionServiceTest {

    @Mock
    CotizacionRepository repository;

    @InjectMocks
    CotizacionService service;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void guardarItems_callsRepositoryMethods() {
        CotizacionDetalleItemRequest item = new CotizacionDetalleItemRequest();
        item.setIdServicio(1);
        item.setCantidad(2);
        item.setPrecioUnitario(BigDecimal.valueOf(100));
        item.setIdTipoMoneda(1);
        item.setIdPeriodicidad(2);

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(id);
        when(repository.findById(id)).thenReturn(Optional.of(cotizacion));

        doNothing().when(repository).deleteDetallesByCotizacion(anyString());
        doNothing().when(repository).insertDetalle(anyString(), anyInt(), anyInt(), anyInt(), any(BigDecimal.class), anyInt(), anyInt(), any(), any(), any(), any());
        doNothing().when(repository).deleteTotalesByCotizacion(anyString());
        
        List<Object[]> totales = new ArrayList<>();
        totales.add(new Object[]{1, BigDecimal.valueOf(200)});
        when(repository.calcularTotalesPorMoneda(anyString())).thenReturn(totales);
        doNothing().when(repository).insertTotal(anyString(), anyInt(), any(BigDecimal.class));

        service.guardarItems(id, Arrays.asList(item));

        verify(repository).deleteDetallesByCotizacion(id.toString());
        verify(repository).insertDetalle(anyString(), anyInt(), anyInt(), anyInt(), any(BigDecimal.class), anyInt(), anyInt(), any(), any(), any(), any());
        verify(repository).deleteTotalesByCotizacion(id.toString());
        verify(repository).insertTotal(anyString(), anyInt(), any(BigDecimal.class));
    }
}

