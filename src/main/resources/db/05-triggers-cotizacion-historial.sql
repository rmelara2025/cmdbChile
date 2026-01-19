-- ===========================================================================
-- Script: Triggers para Control de Historial de Cotizaciones
-- Descripción: Registra automáticamente cambios de estado y versión en cotizacionhistorial
-- ===========================================================================
-- Eliminar triggers existentes si existen
DROP TRIGGER IF EXISTS `trg_cotizacion_primer_estado`;
DROP TRIGGER IF EXISTS `trg_cotizacion_actualizar_historial`;
-- ===========================================================================
-- TRIGGER 1: Registrar estado inicial al crear cotización
-- ===========================================================================
DELIMITER $$ CREATE TRIGGER `trg_cotizacion_primer_estado`
AFTER
INSERT ON `cotizacion` FOR EACH ROW BEGIN -- Registrar el estado inicial de la cotización
INSERT INTO cotizacionhistorial (
        idCotizacion,
        idEstadoAnterior,
        idEstadoNuevo,
        idUsuario,
        comentario,
        fechaCambio
    )
VALUES (
        NEW.idcotizacion,
        NULL,
        NEW.idestadoCotizacion,
        NEW.idUsuarioCreacion,
        CONCAT(
            'Cotización creada - Versión: ',
            COALESCE(NEW.version, 1)
        ),
        NOW()
    );
END $$ DELIMITER;
-- ===========================================================================
-- TRIGGER 2: Registrar cambios de estado o versión al actualizar cotización
-- ===========================================================================
DELIMITER $$ CREATE TRIGGER `trg_cotizacion_actualizar_historial`
AFTER
UPDATE ON `cotizacion` FOR EACH ROW BEGIN
DECLARE v_comentario VARCHAR(500);
DECLARE v_cambio_detectado BOOLEAN DEFAULT FALSE;
-- Construir comentario según el tipo de cambio
SET v_comentario = '';
-- Detectar cambio de estado
IF OLD.idestadoCotizacion != NEW.idestadoCotizacion THEN
SET v_cambio_detectado = TRUE;
SET v_comentario = CONCAT(
        'Cambio de estado',
        CASE
            WHEN OLD.version != NEW.version THEN CONCAT(
                ' y versión (',
                OLD.version,
                ' → ',
                NEW.version,
                ')'
            )
            ELSE ''
        END
    );
-- Registrar cambio de estado
INSERT INTO cotizacionhistorial (
        idCotizacion,
        idEstadoAnterior,
        idEstadoNuevo,
        idUsuario,
        comentario,
        fechaCambio
    )
VALUES (
        NEW.idcotizacion,
        OLD.idestadoCotizacion,
        NEW.idestadoCotizacion,
        NEW.idUsuarioCreacion,
        v_comentario,
        NOW()
    );
-- Detectar cambio de versión SIN cambio de estado
ELSEIF OLD.version != NEW.version THEN
SET v_cambio_detectado = TRUE;
SET v_comentario = CONCAT(
        'Nueva versión creada: ',
        OLD.version,
        ' → ',
        NEW.version
    );
-- Registrar cambio de versión
INSERT INTO cotizacionhistorial (
        idCotizacion,
        idEstadoAnterior,
        idEstadoNuevo,
        idUsuario,
        comentario,
        fechaCambio
    )
VALUES (
        NEW.idcotizacion,
        OLD.idestadoCotizacion,
        NEW.idestadoCotizacion,
        NEW.idUsuarioCreacion,
        v_comentario,
        NOW()
    );
END IF;
-- Actualizar fechaModificacion si hubo algún cambio relevante
IF v_cambio_detectado
OR OLD.observacion != NEW.observacion
OR OLD.fechaVigenciaDesde != NEW.fechaVigenciaDesde
OR OLD.fechaVigenciaHasta != NEW.fechaVigenciaHasta THEN -- Actualizar fecha de modificación (solo si no se actualizó ya en la misma transacción)
IF OLD.fechaModificacion = NEW.fechaModificacion THEN
UPDATE cotizacion
SET fechaModificacion = NOW()
WHERE idcotizacion = NEW.idcotizacion;
END IF;
END IF;
END $$ DELIMITER;
-- ===========================================================================
-- Verificación de triggers creados
-- ===========================================================================
SELECT TRIGGER_NAME as 'Trigger',
    EVENT_MANIPULATION as 'Evento',
    EVENT_OBJECT_TABLE as 'Tabla',
    ACTION_TIMING as 'Momento'
FROM information_schema.TRIGGERS
WHERE EVENT_OBJECT_SCHEMA = DATABASE()
    AND EVENT_OBJECT_TABLE = 'cotizacion'
ORDER BY ACTION_TIMING,
    EVENT_MANIPULATION;
-- ===========================================================================
-- NOTAS DE IMPLEMENTACIÓN
-- ===========================================================================
-- 
-- 1. TRIGGER trg_cotizacion_primer_estado (AFTER INSERT):
--    - Se ejecuta al crear una nueva cotización
--    - Registra el estado inicial con idEstadoAnterior = NULL
--    - Incluye la versión inicial en el comentario
--
-- 2. TRIGGER trg_cotizacion_actualizar_historial (AFTER UPDATE):
--    - Se ejecuta al actualizar una cotización existente
--    - Detecta cambios en:
--      * idestadoCotizacion (cambio de estado)
--      * version (nueva versión)
--      * Ambos simultáneamente
--    - Genera comentarios descriptivos del cambio
--    - Actualiza automáticamente fechaModificacion
--
-- 3. CAMPOS REGISTRADOS:
--    - idCotizacion: UUID de la cotización modificada
--    - idEstadoAnterior: Estado previo al cambio
--    - idEstadoNuevo: Estado posterior al cambio
--    - idUsuario: Usuario que realizó el cambio
--    - comentario: Descripción automática del cambio
--    - fechaCambio: Timestamp del cambio
--
-- 4. MEJORAS PENDIENTES OPCIONALES:
--    - Capturar IP del usuario (requiere modificación en aplicación)
--    - Agregar motivo de rechazo (debe venir desde la aplicación)
--    - Registrar cambios en otros campos relevantes
--
-- ===========================================================================