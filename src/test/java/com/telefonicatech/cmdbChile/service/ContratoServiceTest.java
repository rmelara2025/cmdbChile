package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.ContratoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContratoResponse;
import com.telefonicatech.cmdbChile.model.Contrato;
import com.telefonicatech.cmdbChile.repository.cotizaciones.ContratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContratoServiceTest {

    @Mock
    ContratoRepository contratoRepository;

    @InjectMocks
    ContratoService service;

    private ContratoRequest req;

    @BeforeEach
    void setUp() {
        req = new ContratoRequest();
        req.setRutCliente("99598680-9");
        req.setFechaInicio(LocalDate.of(2024,1,1));
        req.setFechaTermino(LocalDate.of(2024,12,31));
        req.setObservacion("obs");
        req.setTipoCodigoProyecto("CHI");
        req.setCodigoProyecto("CHI-123");
    }

    @Test
    void crearContrato_happyPath_returnsResponse() {
        Contrato saved = new Contrato();
        saved.setIdContrato(UUID.randomUUID());
        saved.setRutCliente(req.getRutCliente());
        saved.setCodChi("CHI-123");
        saved.setFechaInicio(req.getFechaInicio());
        saved.setFechaTermino(req.getFechaTermino());
        saved.setObservacion(req.getObservacion());

        when(contratoRepository.save(org.mockito.ArgumentMatchers.any(Contrato.class))).thenReturn(saved);

        ContratoResponse resp = service.crearContrato(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getIdContrato()).isEqualTo(saved.getIdContrato());
        assertThat(resp.getCodChi()).isEqualTo("CHI-123");
    }

    @Test
    void crearContrato_invalidDates_throws() {
        req.setFechaTermino(LocalDate.of(2023,12,31)); // before inicio
        assertThrows(IllegalArgumentException.class, () -> service.crearContrato(req));
    }
}

