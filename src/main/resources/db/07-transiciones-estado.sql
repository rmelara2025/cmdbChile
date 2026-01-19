-- ============================================
-- Script: Transiciones de Estado de Cotización
-- Descripción: Sistema parametrizable de cambios de estado por rol
-- ============================================

-- Tabla de transiciones de estado permitidas
CREATE TABLE IF NOT EXISTS transicionestado (
    idTransicion INT AUTO_INCREMENT PRIMARY KEY,
    idEstadoOrigen INT NULL COMMENT 'NULL = desde cualquier estado',
    idEstadoDestino INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    requiereComentario BOOLEAN DEFAULT FALSE COMMENT 'Si debe obligar comentario al cambiar',
    requiereMotivoRechazo BOOLEAN DEFAULT FALSE COMMENT 'Si debe obligar motivo de rechazo',
    activo BOOLEAN DEFAULT TRUE,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_transicionestado_origen FOREIGN KEY (idEstadoOrigen) 
        REFERENCES estadocotizacion(idestadoCotizacion),
    CONSTRAINT fk_transicionestado_destino FOREIGN KEY (idEstadoDestino) 
        REFERENCES estadocotizacion(idestadoCotizacion),
    CONSTRAINT uk_transicion UNIQUE (idEstadoOrigen, idEstadoDestino)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Define las transiciones de estado permitidas en el sistema';

-- Tabla intermedia: qué roles pueden ejecutar cada transición
CREATE TABLE IF NOT EXISTS transicionestadorol (
    idTransicion INT NOT NULL,
    idRol INT NOT NULL,
    fechaAsignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (idTransicion, idRol),
    CONSTRAINT fk_transicionestadorol_transicion FOREIGN KEY (idTransicion) 
        REFERENCES transicionestado(idTransicion) ON DELETE CASCADE,
    CONSTRAINT fk_transicionestadorol_rol FOREIGN KEY (idRol) 
        REFERENCES rol(idrol) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Relaciona transiciones de estado con roles autorizados';

-- ============================================
-- INSERTS: Transiciones de Estado
-- ============================================

-- NOTA: Asumiendo los siguientes IDs de estados:
-- 1 = BORRADOR
-- 2 = EN_REVISIÓN  
-- 3 = APROBADA
-- 4 = VIGENTE
-- 5 = REEMPLAZADA
-- 6 = CANCELADA
-- 7 = RECHAZADA
-- 8 = VENCIDA

-- Verificar los IDs correctos con:
-- SELECT idestadoCotizacion, nombre FROM estadocotizacion ORDER BY orden;

-- ============================================
-- Desde BORRADOR (1)
-- ============================================

-- BORRADOR → EN_REVISIÓN (cualquier usuario que tenga el permiso de modificar)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (1, 2, 'Enviar a revisión', FALSE);

-- BORRADOR → CANCELADA (solo administrativo)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (1, 6, 'Cancelar borrador', TRUE);

-- ============================================
-- Desde EN_REVISIÓN (2)
-- ============================================

-- EN_REVISIÓN → APROBADA (gerencial y administrativo)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (2, 3, 'Aprobar cotización', TRUE);

-- EN_REVISIÓN → RECHAZADA (gerencial y administrativo)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario, requiereMotivoRechazo) 
VALUES (2, 7, 'Rechazar cotización', TRUE, TRUE);

-- EN_REVISIÓN → BORRADOR (devolver a edición)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (2, 1, 'Devolver a borrador para correcciones', TRUE);

-- ============================================
-- Desde APROBADA (3)
-- ============================================

-- APROBADA → VIGENTE (administrativo activa la cotización)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (3, 4, 'Activar cotización', FALSE);

-- APROBADA → CANCELADA (solo administrativo)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (3, 6, 'Cancelar cotización aprobada', TRUE);

-- ============================================
-- Desde VIGENTE (4)
-- ============================================

-- VIGENTE → REEMPLAZADA (cuando se crea una nueva versión)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (4, 5, 'Reemplazar por nueva versión', TRUE);

-- VIGENTE → CANCELADA (solo administrativo)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (4, 6, 'Cancelar cotización vigente', TRUE);

-- VIGENTE → VENCIDA (puede ser automático o manual)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (4, 8, 'Marcar como vencida', FALSE);

-- ============================================
-- Desde RECHAZADA (7)
-- ============================================

-- RECHAZADA → BORRADOR (dar segunda oportunidad)
INSERT INTO transicionestado (idEstadoOrigen, idEstadoDestino, descripcion, requiereComentario) 
VALUES (7, 1, 'Reabrir cotización rechazada', TRUE);

-- ============================================
-- INSERTS: Asignación de Roles a Transiciones
-- ============================================

-- NOTA: Asumiendo los siguientes IDs de roles:
-- 1 = Administrativo
-- 2 = Rol gerencial / team leader
-- 3 = Solo vista VIP
-- 4 = Solo vista

-- Verificar los IDs correctos con:
-- SELECT idrol, nombreRol FROM rol;

-- ============================================
-- Transición 1: BORRADOR → EN_REVISIÓN
-- Pueden: Administrativo (1), Gerencial (2)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (1, 1), (1, 2);

-- ============================================
-- Transición 2: BORRADOR → CANCELADA
-- Pueden: Solo Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (2, 1);

-- ============================================
-- Transición 3: EN_REVISIÓN → APROBADA
-- Pueden: Gerencial (2), Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (3, 1), (3, 2);

-- ============================================
-- Transición 4: EN_REVISIÓN → RECHAZADA
-- Pueden: Gerencial (2), Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (4, 1), (4, 2);

-- ============================================
-- Transición 5: EN_REVISIÓN → BORRADOR
-- Pueden: Gerencial (2), Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (5, 1), (5, 2);

-- ============================================
-- Transición 6: APROBADA → VIGENTE
-- Pueden: Solo Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (6, 1);

-- ============================================
-- Transición 7: APROBADA → CANCELADA
-- Pueden: Solo Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (7, 1);

-- ============================================
-- Transición 8: VIGENTE → REEMPLAZADA
-- Pueden: Administrativo (1), Gerencial (2)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (8, 1), (8, 2);

-- ============================================
-- Transición 9: VIGENTE → CANCELADA
-- Pueden: Solo Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (9, 1);

-- ============================================
-- Transición 10: VIGENTE → VENCIDA
-- Pueden: Administrativo (1), Gerencial (2)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (10, 1), (10, 2);

-- ============================================
-- Transición 11: RECHAZADA → BORRADOR
-- Pueden: Solo Administrativo (1)
-- ============================================
INSERT INTO transicionestadorol (idTransicion, idRol) VALUES (11, 1);

-- ============================================
-- Consultas útiles para verificar
-- ============================================

-- Ver todas las transiciones configuradas:
-- SELECT 
--     te.idTransicion,
--     COALESCE(ec1.nombre, 'CUALQUIERA') as desde,
--     ec2.nombre as hacia,
--     te.descripcion,
--     GROUP_CONCAT(r.nombreRol SEPARATOR ', ') as roles_permitidos
-- FROM transicionestado te
-- LEFT JOIN estadocotizacion ec1 ON te.idEstadoOrigen = ec1.idestadoCotizacion
-- JOIN estadocotizacion ec2 ON te.idEstadoDestino = ec2.idestadoCotizacion
-- LEFT JOIN transicionestadorol ter ON te.idTransicion = ter.idTransicion
-- LEFT JOIN rol r ON ter.idRol = r.idrol
-- WHERE te.activo = TRUE
-- GROUP BY te.idTransicion, ec1.nombre, ec2.nombre, te.descripcion
-- ORDER BY te.idTransicion;

-- Ver qué puede hacer un rol específico:
-- SELECT 
--     COALESCE(ec1.nombre, 'CUALQUIERA') as desde,
--     ec2.nombre as hacia,
--     te.descripcion
-- FROM transicionestado te
-- LEFT JOIN estadocotizacion ec1 ON te.idEstadoOrigen = ec1.idestadoCotizacion
-- JOIN estadocotizacion ec2 ON te.idEstadoDestino = ec2.idestadoCotizacion
-- JOIN transicionestadorol ter ON te.idTransicion = ter.idTransicion
-- WHERE ter.idRol = 1  -- Cambiar por el ID del rol
--   AND te.activo = TRUE
-- ORDER BY ec1.orden, ec2.orden;
