package com.telefonicatech.cmdbChile.service.usuarios;

import com.telefonicatech.cmdbChile.repository.usuarios.TransicionEstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TransicionEstadoService {

    private final TransicionEstadoRepository transicionEstadoRepository;

    public TransicionEstadoService(TransicionEstadoRepository transicionEstadoRepository) {
        this.transicionEstadoRepository = transicionEstadoRepository;
    }

    /**
     * Obtiene las acciones (transiciones) que un usuario puede realizar desde un
     * estado específico
     * 
     * @param idUsuario      ID del usuario
     * @param idEstadoActual ID del estado actual de la cotización
     * @return Lista de acciones disponibles con información del estado destino
     */
    public List<Map<String, Object>> obtenerAccionesDisponibles(String idUsuario, Integer idEstadoActual) {
        return transicionEstadoRepository.obtenerAccionesDisponibles(idUsuario, idEstadoActual);
    }

    /**
     * Valida si un usuario tiene permiso para realizar una transición de estado
     * 
     * @param idUsuario       ID del usuario que intenta realizar la transición
     * @param idEstadoOrigen  ID del estado actual
     * @param idEstadoDestino ID del estado al que se quiere cambiar
     * @throws IllegalStateException si la transición no está permitida
     */
    public void validarTransicion(String idUsuario, Integer idEstadoOrigen, Integer idEstadoDestino) {
        boolean puedeTransicionar = transicionEstadoRepository.puedeTransicionar(
                idUsuario,
                idEstadoOrigen,
                idEstadoDestino);

        if (!puedeTransicionar) {
            throw new IllegalStateException(
                    String.format("El usuario %s no tiene permiso para cambiar del estado %d al estado %d",
                            idUsuario, idEstadoOrigen, idEstadoDestino));
        }
    }

    /**
     * Valida que los campos obligatorios estén presentes según la configuración de
     * la transición
     * 
     * @param idEstadoOrigen  ID del estado actual
     * @param idEstadoDestino ID del estado destino
     * @param comentario      Comentario proporcionado (puede ser null)
     * @param motivoRechazo   Motivo de rechazo proporcionado (puede ser null)
     * @throws IllegalArgumentException si faltan campos obligatorios
     */
    public void validarCamposObligatorios(Integer idEstadoOrigen, Integer idEstadoDestino,
            String comentario, String motivoRechazo) {
        boolean requiereComentario = transicionEstadoRepository.requiereComentario(idEstadoOrigen, idEstadoDestino);
        boolean requiereMotivoRechazo = transicionEstadoRepository.requiereMotivoRechazo(idEstadoOrigen,
                idEstadoDestino);

        if (requiereComentario && (comentario == null || comentario.trim().isEmpty())) {
            throw new IllegalArgumentException("Esta transición requiere un comentario obligatorio");
        }

        if (requiereMotivoRechazo && (motivoRechazo == null || motivoRechazo.trim().isEmpty())) {
            throw new IllegalArgumentException("Esta transición requiere un motivo de rechazo obligatorio");
        }
    }

    /**
     * Verifica si una transición requiere comentario obligatorio
     * 
     * @param idEstadoOrigen  ID del estado actual
     * @param idEstadoDestino ID del estado destino
     * @return true si la transición requiere comentario
     */
    public boolean requiereComentario(Integer idEstadoOrigen, Integer idEstadoDestino) {
        return transicionEstadoRepository.requiereComentario(idEstadoOrigen, idEstadoDestino);
    }
}
