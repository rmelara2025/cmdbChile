-- ============================================
-- SCRIPT: Verificación de servicios duplicados y disponibles
-- PROPÓSITO: Validar integridad de datos de servicios
-- ============================================
USE cmdb_tech;
-- ============================================
-- 1. VERIFICAR SERVICIOS DUPLICADOS
-- ============================================
SELECT '🔍 VERIFICACIÓN DE DUPLICADOS' as titulo;
-- Buscar servicios con nombres duplicados
SELECT nombreServicio,
    COUNT(*) as cantidad,
    GROUP_CONCAT(
        idServicio
        ORDER BY idServicio
    ) as ids_duplicados,
    CASE
        WHEN COUNT(*) > 1 THEN '❌ DUPLICADO'
        ELSE '✅ OK'
    END as estado
FROM servicio
GROUP BY nombreServicio
HAVING COUNT(*) > 1
ORDER BY cantidad DESC;
-- Si no hay duplicados, mostrar mensaje
SELECT CASE
        WHEN (
            SELECT COUNT(*)
            FROM (
                    SELECT nombreServicio
                    FROM servicio
                    GROUP BY nombreServicio
                    HAVING COUNT(*) > 1
                ) as duplicados
        ) = 0 THEN '✅ No se encontraron servicios duplicados'
        ELSE '❌ Se encontraron servicios duplicados (ver arriba)'
    END as resultado_duplicados;
-- ============================================
-- 2. LISTAR TODOS LOS SERVICIOS DISPONIBLES
-- ============================================
SELECT '📋 SERVICIOS DISPONIBLES' as titulo;
SELECT s.idServicio,
    s.nombreServicio,
    fs.nombreFamilia as familia,
    CASE
        WHEN s.nombreServicio IS NULL
        OR TRIM(s.nombreServicio) = '' THEN '❌ SIN NOMBRE'
        ELSE '✅ OK'
    END as validacion
FROM servicio s
    LEFT JOIN familiaservicio fs ON s.idFamilia = fs.idFamilia
ORDER BY fs.nombreFamilia,
    s.nombreServicio;
-- ============================================
-- 3. CONTAR SERVICIOS POR FAMILIA
-- ============================================
SELECT '📊 ESTADÍSTICAS POR FAMILIA' as titulo;
SELECT fs.nombreFamilia as familia,
    COUNT(s.idServicio) as total_servicios,
    CASE
        WHEN COUNT(s.idServicio) = 0 THEN '⚠️ SIN SERVICIOS'
        WHEN COUNT(s.idServicio) < 3 THEN '⚠️ POCOS SERVICIOS'
        ELSE '✅ OK'
    END as estado
FROM familiaservicio fs
    LEFT JOIN servicio s ON fs.idFamilia = s.idFamilia
GROUP BY fs.idFamilia,
    fs.nombreFamilia
ORDER BY total_servicios DESC;
-- ============================================
-- 4. VERIFICAR SERVICIOS SIN FAMILIA
-- ============================================
SELECT '⚠️ SERVICIOS HUÉRFANOS (sin familia asignada)' as titulo;
SELECT idServicio,
    nombreServicio,
    '❌ SIN FAMILIA' as problema
FROM servicio
WHERE idFamilia IS NULL
ORDER BY nombreServicio;
-- Si no hay huérfanos, mostrar mensaje
SELECT CASE
        WHEN (
            SELECT COUNT(*)
            FROM servicio
            WHERE idFamilia IS NULL
        ) = 0 THEN '✅ Todos los servicios tienen familia asignada'
        ELSE CONCAT(
            '⚠️ Hay ',
            (
                SELECT COUNT(*)
                FROM servicio
                WHERE idFamilia IS NULL
            ),
            ' servicios sin familia'
        )
    END as resultado_huerfanos;
-- ============================================
-- 5. VERIFICAR FAMILIAS DISPONIBLES
-- ============================================
SELECT '📁 FAMILIAS DE SERVICIOS DISPONIBLES' as titulo;
SELECT idFamilia,
    nombreFamilia,
    descripcion
FROM familiaservicio
ORDER BY nombreFamilia;
-- ============================================
-- 6. RESUMEN GENERAL
-- ============================================
SELECT '📈 RESUMEN GENERAL' as titulo;
SELECT 'Total de Servicios' as metrica,
    COUNT(*) as valor
FROM servicio
UNION ALL
SELECT 'Servicios con Familia' as metrica,
    COUNT(*) as valor
FROM servicio
WHERE idFamilia IS NOT NULL
UNION ALL
SELECT 'Servicios sin Familia' as metrica,
    COUNT(*) as valor
FROM servicio
WHERE idFamilia IS NULL
UNION ALL
SELECT 'Total de Familias' as metrica,
    COUNT(*) as valor
FROM familiaservicio
UNION ALL
SELECT 'Familias con Servicios' as metrica,
    COUNT(DISTINCT s.idFamilia) as valor
FROM servicio s
WHERE s.idFamilia IS NOT NULL;