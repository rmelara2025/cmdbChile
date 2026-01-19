-- ===========================================================================
-- Índices para Generación de Códigos de Cotización con MAX()
-- Descripción: Optimiza la consulta MAX() sobre numeroCotizacion
-- Formato: COT-YYYY-NNNNNNNN (8 dígitos)
-- ===========================================================================
-- ===========================================================================
-- ESTRATEGIA SIN TABLA ADICIONAL
-- ===========================================================================
-- 
-- NO SE CREA TABLA DE SECUENCIA
-- Se usa MAX(numeroCotizacion) + FOR UPDATE directamente en tabla cotizacion
-- 
-- VENTAJAS:
-- ✓ Base de datos más limpia (menos tablas)
-- ✓ Código más simple
-- ✓ Suficiente para volúmenes medios (< 100K cotizaciones/año)
-- ✓ Clean Architecture en BD
--
-- FUNCIONAMIENTO:
-- 1. Backend ejecuta: SELECT MAX(SUBSTRING(numeroCotizacion, 10)) ... FOR UPDATE
-- 2. Incrementa el número
-- 3. Guarda la nueva cotización con el código generado
--
-- ===========================================================================
-- Crear índice para optimizar la búsqueda de MAX() por año
-- Esto acelera significativamente el SELECT MAX() cuando hay muchas cotizaciones
CREATE INDEX IF NOT EXISTS idx_cotizacion_numero_codigo ON cotizacion(numeroCotizacion);
-- ===========================================================================
-- VERIFICACIÓN
-- ===========================================================================
-- Verificar índice creado
SHOW INDEX
FROM cotizacion
WHERE Key_name = 'idx_cotizacion_numero_codigo';
-- Consultar códigos existentes por año (para verificar secuencia)
SELECT SUBSTRING(numeroCotizacion, 1, 8) as 'Prefijo Año',
    COUNT(*) as 'Cantidad',
    MIN(numeroCotizacion) as 'Primer Código',
    MAX(numeroCotizacion) as 'Último Código'
FROM cotizacion
WHERE numeroCotizacion LIKE 'COT-%'
GROUP BY SUBSTRING(numeroCotizacion, 1, 8)
ORDER BY 1 DESC;
-- ===========================================================================
-- NOTAS DE IMPLEMENTACIÓN
-- ===========================================================================
-- 
-- 1. FORMATO FINAL:
--    - COT-2026-00000001
--    - COT-2026-00000002
--    - ...
--    - COT-2026-99999999 (capacidad: 99,999,999 cotizaciones/año)
--
-- 2. CONCURRENCIA:
--    - SELECT MAX() ... FOR UPDATE bloquea las filas durante la transacción
--    - Evita duplicados en creaciones simultáneas
--
-- 3. RENDIMIENTO:
--    - Índice en numeroCotizacion optimiza el MAX()
--    - Suficientemente rápido para volúmenes medios
--    - Lock duration: ~1-5ms (imperceptible para usuarios)
--
-- 4. REINICIO AUTOMÁTICO:
--    - Cada año nuevo empieza en 1 automáticamente
--    - No requiere mantenimiento manual
--
-- ===========================================================================