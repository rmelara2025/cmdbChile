-- ============================================
-- Script de Permisos y Roles
-- Gestión de permisos granulares por rol
-- ============================================

-- Tabla de Permisos
CREATE TABLE IF NOT EXISTS permisos (
    idpermiso INT AUTO_INCREMENT PRIMARY KEY,
    codigopermiso VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    modulo VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    fechacreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_codigo_permiso UNIQUE (codigopermiso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Roles (si no existe ya)
-- Nombres de campos según schema real: idrol, nombreRol, descripcionRol
CREATE TABLE IF NOT EXISTS rol (
    idrol INT AUTO_INCREMENT PRIMARY KEY,
    nombreRol VARCHAR(100) NOT NULL UNIQUE,
    descripcionRol TEXT,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT uk_nombre_rol UNIQUE (nombreRol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla intermedia Rol-Permisos (relación muchos-a-muchos)
-- Nombres según schema real: rolpermisos (sin guion bajo), idrol, idpermiso, fechaasignacion
CREATE TABLE IF NOT EXISTS rolpermisos (
    idrol INT NOT NULL,
    idpermiso INT NOT NULL,
    fechaasignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idrol, idpermiso),
    CONSTRAINT fk_rol_permisos_rol FOREIGN KEY (idrol) REFERENCES rol(idrol) ON DELETE CASCADE,
    CONSTRAINT fk_rol_permisos_permiso FOREIGN KEY (idpermiso) REFERENCES permisos(idpermiso) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- INSERTS: Permisos
-- ============================================

INSERT INTO permisos (codigopermiso, descripcion, modulo) VALUES
('VER_TODO', 'Acceso completo a todos los módulos y funcionalidades', 'GENERAL'),
('VER_SERVICIOS_CONTRATOS', 'Ver servicios y contratos (sin recurrencias)', 'SERVICIOS'),
('VER_RECURRENCIAS', 'Ver información de recurrencias', 'SERVICIOS'),
('VER_REPORTES', 'Acceso a módulo de reportes', 'REPORTES'),
('EXPORTAR_REPORTES', 'Exportar reportes a Excel/PDF', 'REPORTES'),
('MODIFICAR', 'Modificar cotizaciones, servicios y contratos', 'COTIZACIONES'),
('APROBAR', 'Aprobar cotizaciones y solicitudes', 'COTIZACIONES'),
('ALTA_USUARIOS', 'Dar de alta nuevos usuarios', 'USUARIOS'),
('BAJA_USUARIOS', 'Dar de baja usuarios existentes', 'USUARIOS'),
('GESTIONAR_USUARIOS', 'Gestión completa de usuarios', 'USUARIOS'),
('VER_DASHBOARD', 'Acceso al dashboard principal', 'GENERAL'),
('VER_COTIZACIONES', 'Ver listado de cotizaciones', 'COTIZACIONES'),
('CREAR_COTIZACIONES', 'Crear nuevas cotizaciones', 'COTIZACIONES'),
('ELIMINAR_COTIZACIONES', 'Eliminar cotizaciones', 'COTIZACIONES'),
('VER_CLIENTES', 'Ver listado de clientes', 'CLIENTES'),
('GESTIONAR_CLIENTES', 'Crear, modificar y eliminar clientes', 'CLIENTES');

-- ============================================
-- INSERTS: Roles
-- ============================================

INSERT INTO rol (nombreRol, descripcionRol) VALUES
('Administrativo', 'Acceso a todo, modificar, reportes, altas, bajas, puede dar de alta usuarios'),
('Rol gerencial / team leader', 'Acceso a todo, reportes, vistas, alta de aprobación alta, alta de usuarios'),
('Solo vista VIP', 'Rol solo vista a todo, sin poder modificar ni aprobar, solo vista a todo, puede sacar reporte'),
('Solo vista', 'Rol solo vista a servicios y contratos (sin recurrencias, ni reportes)');

-- ============================================
-- INSERTS: Asignación de Permisos por Rol
-- ============================================

-- ROL: Administrativo (idrol = 1)
-- Tiene TODOS los permisos
INSERT INTO rolpermisos (idrol, idpermiso) 
SELECT 1, idpermiso FROM permisos;

-- ROL: Rol gerencial / team leader (idrol = 2)
-- Acceso a todo, reportes, vistas, aprobar, alta de usuarios (sin modificar, sin bajas)
INSERT INTO rolpermisos (idrol, idpermiso) VALUES
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_TODO')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_RECURRENCIAS')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_REPORTES')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'EXPORTAR_REPORTES')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'APROBAR')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'ALTA_USUARIOS')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_DASHBOARD')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_COTIZACIONES')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'CREAR_COTIZACIONES')),
(2, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_CLIENTES'));

-- ROL: Solo vista VIP (idrol = 3)
-- Ver todo, exportar reportes (sin modificar, sin aprobar)
INSERT INTO rolpermisos (idrol, idpermiso) VALUES
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_TODO')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_RECURRENCIAS')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_REPORTES')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'EXPORTAR_REPORTES')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_DASHBOARD')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_COTIZACIONES')),
(3, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_CLIENTES'));

-- ROL: Solo vista (idrol = 4)
-- Ver solo servicios y contratos (sin recurrencias, sin reportes)
INSERT INTO rolpermisos (idrol, idpermiso) VALUES
(4, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_SERVICIOS_CONTRATOS')),
(4, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_DASHBOARD')),
(4, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_COTIZACIONES')),
(4, (SELECT idpermiso FROM permisos WHERE codigopermiso = 'VER_CLIENTES'));

-- ============================================
-- Consultas de Verificación
-- ============================================

-- Ver todos los permisos
-- SELECT * FROM permisos ORDER BY modulo, codigo_permiso;

-- Ver todos los roles
-- Ver todos los roles
-- SELECT * FROM rol;

-- Ver permisos por rol
-- SELECT r.nombreRol, p.codigopermiso, p.descripcion, p.modulo
-- FROM rol r
-- JOIN rolpermisos rp ON r.idrol = rp.idrol
-- JOIN permisos p ON rp.idpermiso = p.idpermiso
-- ORDER BY r.idrol, p.modulo, p.codigopermiso;

-- Ver qué roles tienen un permiso específico
-- SELECT r.nombreRol, p.codigopermiso
-- FROM rol r
-- JOIN rolpermisos rp ON r.idrol = rp.idrol
-- JOIN permisos p ON rp.idpermiso = p.idpermiso
-- WHERE p.codigopermiso = 'MODIFICAR';

-- ============================================
-- Notas de Implementación
-- ============================================

/*
RESUMEN DE PERMISOS POR ROL:

1. Administrativo:
   - TODOS los permisos (16 permisos)
   
2. Rol gerencial / team leader:
   - VER_TODO, VER_RECURRENCIAS, VER_REPORTES, EXPORTAR_REPORTES
   - APROBAR, ALTA_USUARIOS
   - VER_DASHBOARD, VER_COTIZACIONES, CREAR_COTIZACIONES, VER_CLIENTES
   - NO tiene: MODIFICAR, BAJA_USUARIOS, ELIMINAR_COTIZACIONES, GESTIONAR_CLIENTES

3. Solo vista VIP:
   - VER_TODO, VER_RECURRENCIAS, VER_REPORTES, EXPORTAR_REPORTES
   - VER_DASHBOARD, VER_COTIZACIONES, VER_CLIENTES
   - NO tiene: MODIFICAR, APROBAR, gestión de usuarios

4. Solo vista:
   - VER_SERVICIOS_CONTRATOS (limitado)
   - VER_DASHBOARD, VER_COTIZACIONES, VER_CLIENTES
   - NO tiene: VER_RECURRENCIAS, VER_REPORTES, MODIFICAR, APROBAR, gestión usuarios

ENDPOINT RECOMENDADO:
GET /api/usuario/admin/roles/{idUsuario}

Response:
[
  {
    "idrol": 1,
    "nombreRol": "Administrativo",
    "descripcion": "...",
    "permisos": ["VER_TODO", "MODIFICAR", "APROBAR", ...]
  }
]
*/
