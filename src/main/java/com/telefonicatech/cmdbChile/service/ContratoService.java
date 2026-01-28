package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.ContratoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContratoResponse;
import com.telefonicatech.cmdbChile.model.Contrato;
import com.telefonicatech.cmdbChile.repository.cotizaciones.ContratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Transactional
    public ContratoResponse crearContrato(ContratoRequest request) {
        // Validar que fechaTermino sea posterior a fechaInicio
        if (request.getFechaTermino().isBefore(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de término debe ser posterior a la fecha de inicio");
        }

        // Crear la entidad Contrato
        Contrato contrato = new Contrato();
        contrato.setRutCliente(request.getRutCliente());
        contrato.setFechaInicio(request.getFechaInicio());
        contrato.setFechaTermino(request.getFechaTermino());
        contrato.setObservacion(request.getObservacion());

        // Asignar el código al campo correcto según el tipo
        switch (request.getTipoCodigoProyecto().toUpperCase()) {
            case "CHI":
                contrato.setCodChi(request.getCodigoProyecto());
                break;
            case "SAP":
                contrato.setCodSap(request.getCodigoProyecto());
                break;
            case "SISON":
                contrato.setCodSison(request.getCodigoProyecto());
                break;
            default:
                throw new IllegalArgumentException(
                        "Tipo de código de proyecto no válido: " + request.getTipoCodigoProyecto() +
                                ". Debe ser CHI, SAP o SISON");
        }

        // Guardar el contrato
        Contrato contratoGuardado = contratoRepository.save(contrato);

        // Retornar respuesta
        return new ContratoResponse(
                contratoGuardado.getIdContrato(),
                contratoGuardado.getRutCliente(),
                contratoGuardado.getCodChi(),
                contratoGuardado.getCodSap(),
                contratoGuardado.getCodSison(),
                contratoGuardado.getFechaInicio(),
                contratoGuardado.getFechaTermino(),
                contratoGuardado.getObservacion());
    }
}
