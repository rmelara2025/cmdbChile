-- ============================================
-- SCRIPT: Desactivar transición manual VIGENTE → REEMPLAZADA
-- PROPÓSITO: El estado REEMPLAZADA debe establecerse automáticamente
--            cuando se versiona una cotización (al modificar items),
--            no como una acción manual desde la interfaz.
-- ============================================
USE cmdb_tech;
-- Desactivar la transición 8: VIGENTE → REEMPLAZADA
-- Esta transición NO debe estar disponible como acción manual
UPDATE transicionestado
SET activo = FALSE,
    descripcion = 'Reemplazar por nueva versión (AUTOMÁTICO - NO MANUAL)'
WHERE idEstadoOrigen = 4
    AND idEstadoDestino = 5
    AND activo = TRUE;
-- Verificar que se desactivó correctamente
SELECT te.idTransicion,
    e1.nombre as estado_origen,
    e2.nombre as estado_destino,
    te.descripcion,
    te.activo,
    CASE
        WHEN te.activo = TRUE THEN '✅ ACTIVA (se muestra en UI)'
        ELSE '❌ INACTIVA (NO se muestra en UI)'
    END as estado_ui
FROM transicionestado te
    INNER JOIN estadocotizacion e1 ON te.idEstadoOrigen = e1.idEstadoCotizacion
    INNER JOIN estadocotizacion e2 ON te.idEstadoDestino = e2.idEstadoCotizacion
WHERE te.idEstadoOrigen = 4
    AND te.idEstadoDestino = 5;
-- Verificar todas las transiciones activas desde VIGENTE
SELECT te.idTransicion,
    e2.nombre as puede_cambiar_a,
    te.descripcion
FROM transicionestado te
    INNER JOIN estadocotizacion e2 ON te.idEstadoDestino = e2.idEstadoCotizacion
WHERE te.idEstadoOrigen = 4
    AND te.activo = TRUE
ORDER BY te.idTransicion;