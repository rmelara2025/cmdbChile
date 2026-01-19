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
CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_nombre_rol UNIQUE (nombre_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla intermedia Rol-Permisos (relación muchos-a-muchos)
CREATE TABLE IF NOT EXISTS rol_permisos (
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_rol, id_permiso),
    CONSTRAINT fk_rol_permisos_rol FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE,
    CONSTRAINT fk_rol_permisos_permiso FOREIGN KEY (id_permiso) REFERENCES permisos(id_permiso) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- INSERTS: Permisos
-- ============================================

INSERT INTO permisos (codigo_permiso, descripcion, modulo) VALUES
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

INSERT INTO roles (nombre_rol, descripcion) VALUES
('Administrativo', 'Acceso a todo, modificar, reportes, altas, bajas, puede dar de alta usuarios'),
('Rol gerencial / team leader', 'Acceso a todo, reportes, vistas, alta de aprobación alta, alta de usuarios'),
('Solo vista VIP', 'Rol solo vista a todo, sin poder modificar ni aprobar, solo vista a todo, puede sacar reporte'),
('Solo vista', 'Rol solo vista a servicios y contratos (sin recurrencias, ni reportes)');

-- ============================================
-- INSERTS: Asignación de Permisos por Rol
-- ============================================

-- ROL: Administrativo (id_rol = 1)
-- Tiene TODOS los permisos
INSERT INTO rol_permisos (id_rol, id_permiso) 
SELECT 1, id_permiso FROM permisos;

-- ROL: Rol gerencial / team leader (id_rol = 2)
-- Acceso a todo, reportes, vistas, aprobar, alta de usuarios (sin modificar, sin bajas)
INSERT INTO rol_permisos (id_rol, id_permiso) VALUES
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_TODO')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_RECURRENCIAS')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_REPORTES')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'EXPORTAR_REPORTES')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'APROBAR')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'ALTA_USUARIOS')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_DASHBOARD')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_COTIZACIONES')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'CREAR_COTIZACIONES')),
(2, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_CLIENTES'));

-- ROL: Solo vista VIP (id_rol = 3)
-- Ver todo, exportar reportes (sin modificar, sin aprobar)
INSERT INTO rol_permisos (id_rol, id_permiso) VALUES
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_TODO')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_RECURRENCIAS')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_REPORTES')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'EXPORTAR_REPORTES')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_DASHBOARD')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_COTIZACIONES')),
(3, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_CLIENTES'));

-- ROL: Solo vista (id_rol = 4)
-- Ver solo servicios y contratos (sin recurrencias, sin reportes)
INSERT INTO rol_permisos (id_rol, id_permiso) VALUES
(4, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_SERVICIOS_CONTRATOS')),
(4, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_DASHBOARD')),
(4, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_COTIZACIONES')),
(4, (SELECT id_permiso FROM permisos WHERE codigo_permiso = 'VER_CLIENTES'));

-- ============================================
-- Consultas de Verificación
-- ============================================

-- Ver todos los permisos
-- SELECT * FROM permisos ORDER BY modulo, codigo_permiso;

-- Ver todos los roles
-- SELECT * FROM roles;

-- Ver permisos por rol
-- SELECT r.nombre_rol, p.codigo_permiso, p.descripcion, p.modulo
-- FROM roles r
-- JOIN rol_permisos rp ON r.id_rol = rp.id_rol
-- JOIN permisos p ON rp.id_permiso = p.id_permiso
-- ORDER BY r.id_rol, p.modulo, p.codigo_permiso;

-- Ver qué roles tienen un permiso específico
-- SELECT r.nombre_rol, p.codigo_permiso
-- FROM roles r
-- JOIN rol_permisos rp ON r.id_rol = rp.id_rol
-- JOIN permisos p ON rp.id_permiso = p.id_permiso
-- WHERE p.codigo_permiso = 'MODIFICAR';

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
