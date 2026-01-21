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
-- TRIGGER 2: Registrar SOLO cambios de VERSIÓN (NO de estado)
-- Los cambios de estado se registran manualmente desde el backend con comentarios del usuario
-- ===========================================================================
DELIMITER $$ CREATE TRIGGER `trg_cotizacion_actualizar_historial`
AFTER
UPDATE ON `cotizacion` FOR EACH ROW BEGIN
DECLARE v_comentario VARCHAR(500);
-- SOLO registrar si cambia la VERSIÓN (no el estado)
-- Los cambios de estado se manejan manualmente desde CotizacionService
IF OLD.version != NEW.version THEN
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
--    - Se ejecuta SOLO al cambiar la VERSIÓN
--    - NO se ejecuta para cambios de estado (evita duplicados)
--    - Los cambios de estado se registran manualmente desde CotizacionService
--      con comentarios y motivos proporcionados por el usuario
--
-- 3. CAMPOS REGISTRADOS EN HISTORIAL:
--    - idCotizacion: UUID de la cotización modificada
--    - idEstadoAnterior: Estado previo al cambio
--    - idEstadoNuevo: Estado posterior al cambio
--    - idUsuario: Usuario que realizó el cambio
--    - comentario: Descripción del cambio (del usuario o automática)
--    - motivoRechazo: Motivo del rechazo si aplica
--    - fechaCambio: Timestamp del cambio
--
-- 4. FLUJO DE CAMBIO DE ESTADO:
--    Backend (CotizacionService) → Valida permisos → Inserta en historial manualmente
--    → Actualiza estado en cotización → Trigger NO se ejecuta (solo versión)
--
-- ===========================================================================