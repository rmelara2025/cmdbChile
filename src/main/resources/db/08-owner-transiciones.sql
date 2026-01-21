-- ============================================
-- SCRIPT: Agregar rol Owner (id=5) a todas las transiciones de estado
-- PROPÓSITO: Permitir que el rol Owner (SystemAdministrator) pueda realizar todas las transiciones
-- ============================================
USE cmdb_chile;
-- Agregar Owner a todas las transiciones existentes
-- Usamos INSERT IGNORE para evitar duplicados si ya existe alguna asignación
-- Transición 1: BORRADOR → EN_REVISIÓN
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (1, 5);
-- Transición 2: EN_REVISIÓN → BORRADOR
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (2, 5);
-- Transición 3: EN_REVISIÓN → APROBADA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (3, 5);
-- Transición 4: EN_REVISIÓN → ANULADA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (4, 5);
-- Transición 5: APROBADA → EN_REVISIÓN
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (5, 5);
-- Transición 6: APROBADA → VIGENTE
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (6, 5);
-- Transición 7: APROBADA → ANULADA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (7, 5);
-- Transición 8: VIGENTE → CANCELADA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (8, 5);
-- Transición 9: VIGENTE → DE_BAJA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (9, 5);
-- Transición 10: VIGENTE → ANULADA
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (10, 5);
-- Transición 11: ANULADA → BORRADOR
INSERT IGNORE INTO transicionestadorol (idTransicion, idRol)
VALUES (11, 5);
-- Verificar que se agregaron correctamente
SELECT te.idTransicion,
    te.descripcion,
    GROUP_CONCAT(
        ter.idRol
        ORDER BY ter.idRol
    ) as roles_permitidos
FROM transicionestado te
    LEFT JOIN transicionestadorol ter ON te.idTransicion = ter.idTransicion
WHERE te.activo = true
GROUP BY te.idTransicion,
    te.descripcion
ORDER BY te.idTransicion;
-- Mostrar específicamente las transiciones del Owner
SELECT te.idTransicion,
    e1.nombre as estado_origen,
    e2.nombre as estado_destino,
    te.descripcion
FROM transicionestadorol ter
    INNER JOIN transicionestado te ON ter.idTransicion = te.idTransicion
    INNER JOIN estadocotizacion e1 ON te.idEstadoOrigen = e1.idEstadoCotizacion
    INNER JOIN estadocotizacion e2 ON te.idEstadoDestino = e2.idEstadoCotizacion
WHERE ter.idRol = 5
ORDER BY te.idTransicion;