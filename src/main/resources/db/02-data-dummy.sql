-- ============================================
-- SCRIPT: Datos dummy para pruebas
-- ============================================
-- ============================================
-- 1. DATOS DE CATÁLOGOS
-- ============================================
-- Periodicidades
INSERT INTO `periodicidad` (`nombre`, `meses`, `descripcion`)
VALUES ('ONE_SHOT', 0, 'Pago único, no recurrente'),
    ('MENSUAL', 1, 'Cobro mensual'),
    ('BIMENSUAL', 2, 'Cobro cada 2 meses'),
    ('TRIMESTRAL', 3, 'Cobro trimestral'),
    ('SEMESTRAL', 6, 'Cobro semestral'),
    ('ANUAL', 12, 'Cobro anual');
-- Estados de cotización
INSERT INTO `estadocotizacion` (`nombre`, `ordern`, `descripcion`)
VALUES ('BORRADOR', 1, 'Cotización en construcción'),
    ('EN_REVISION', 2, 'Enviada para revisión'),
    ('APROBADA', 3, 'Aprobada internamente'),
    ('VIGENTE', 4, 'Cotización actualmente vigente'),
    (
        'REEMPLAZADA',
        5,
        'Reemplazada por una nueva versión'
    ),
    ('ANULADA', 6, 'Anulada por error o corrección'),
    ('CANCELADA', 7, 'Cancelada por el cliente'),
    ('DE_BAJA', 8, 'Dada de baja');
-- ============================================
-- 2. CLIENTES (si no existen)
-- ============================================
INSERT IGNORE INTO `cliente` (
        `rutCliente`,
        `nombreCliente`,
        `razonSocial`,
        `estado`
    )
VALUES (
        '76123456-7',
        'Empresa Demo S.A.',
        'EMPRESA DEMO SOCIEDAD ANONIMA',
        1
    ),
    (
        '77987654-3',
        'Tecnología Avanzada Ltda.',
        'TECNOLOGIA AVANZADA LIMITADA',
        1
    ),
    (
        '78555444-1',
        'Servicios Corporativos SpA',
        'SERVICIOS CORPORATIVOS SPA',
        1
    );
-- ============================================
-- 3. SERVICIOS (si no existen)
-- ============================================
INSERT IGNORE INTO `servicio` (
        `idServicio`,
        `nombre`,
        `descripcion`,
        `idFamilia`
    )
VALUES (
        1,
        'Hosting Cloud Básico',
        'Servicio de hosting en la nube con recursos básicos',
        1
    ),
    (
        2,
        'Hosting Cloud Avanzado',
        'Servicio de hosting en la nube con recursos avanzados',
        1
    ),
    (
        3,
        'Máquina Virtual Standard',
        'VM con configuración estándar',
        2
    ),
    (
        4,
        'Máquina Virtual Premium',
        'VM con alta disponibilidad y recursos premium',
        2
    ),
    (
        5,
        'Backup Diario',
        'Servicio de respaldo diario automático',
        3
    ),
    (
        6,
        'Soporte 24/7',
        'Soporte técnico disponible 24 horas',
        4
    ),
    (
        7,
        'Instalación y Configuración',
        'Servicio de instalación inicial y configuración',
        5
    ),
    (
        8,
        'Licencia Microsoft 365 Business',
        'Licencia empresarial de Microsoft 365',
        6
    ),
    (
        9,
        'Firewall Managed',
        'Servicio de firewall administrado',
        7
    ),
    (
        10,
        'Monitoreo Proactivo',
        'Monitoreo 24/7 de infraestructura',
        8
    );
-- ============================================
-- 4. CONTRATOS
-- ============================================
SET @idContrato1 = UUID_TO_BIN(UUID());
SET @idContrato2 = UUID_TO_BIN(UUID());
SET @idContrato3 = UUID_TO_BIN(UUID());
INSERT INTO `contrato` (
        `idContrato`,
        `rutCliente`,
        `cod_sison`,
        `cod_chi`,
        `cod_sap`,
        `fechaInicio`,
        `fechaTermino`,
        `observacion`
    )
VALUES (
        @idContrato1,
        '76123456-7',
        'SIS-2024-001',
        'CHI-2024-001',
        'SAP-2024-001',
        '2024-01-01',
        '2024-12-31',
        'Contrato principal de infraestructura'
    ),
    (
        @idContrato2,
        '77987654-3',
        'SIS-2024-002',
        'CHI-2024-002',
        'SAP-2024-002',
        '2024-06-01',
        '2025-05-31',
        'Contrato de servicios cloud'
    ),
    (
        @idContrato3,
        '78555444-1',
        'SIS-2024-003',
        'CHI-2024-003',
        'SAP-2024-003',
        '2024-09-01',
        '2025-08-31',
        'Contrato de hosting y soporte'
    );
-- ============================================
-- 5. COTIZACIONES
-- ============================================
-- Contrato 1: 2 cotizaciones (una vigente, una reemplazada)
SET @idCot1V1 = UUID_TO_BIN(UUID());
SET @idCot1V2 = UUID_TO_BIN(UUID());
INSERT INTO `cotizacion` (
        `idCotizacion`,
        `idContrato`,
        `numeroCotizacion`,
        `version`,
        `idEstadoCotizacion`,
        `fechaEmision`,
        `fechaVigenciaDesde`,
        `fechaVigenciaHasta`,
        `observacion`
    )
VALUES -- Cotización 1 - Versión 1 (REEMPLAZADA)
    (
        @idCot1V1,
        @idContrato1,
        'COT-2024-001',
        1,
        5,
        '2024-01-01',
        '2024-01-01',
        '2024-06-30',
        'Primera versión de cotización, reemplazada por cambios en servicios'
    ),
    -- Cotización 1 - Versión 2 (VIGENTE)
    (
        @idCot1V2,
        @idContrato1,
        'COT-2024-001',
        2,
        4,
        '2024-06-15',
        '2024-07-01',
        '2024-12-31',
        'Versión actualizada con nuevos servicios'
    );
-- Contrato 2: 1 cotización vigente
SET @idCot2V1 = UUID_TO_BIN(UUID());
INSERT INTO `cotizacion` (
        `idCotizacion`,
        `idContrato`,
        `numeroCotizacion`,
        `version`,
        `idEstadoCotizacion`,
        `fechaEmision`,
        `fechaVigenciaDesde`,
        `fechaVigenciaHasta`,
        `observacion`
    )
VALUES (
        @idCot2V1,
        @idContrato2,
        'COT-2024-002',
        1,
        4,
        '2024-06-01',
        '2024-06-01',
        '2025-05-31',
        'Cotización inicial de servicios cloud'
    );
-- Contrato 3: 2 cotizaciones vigentes simultáneas
SET @idCot3V1 = UUID_TO_BIN(UUID());
SET @idCot3V2 = UUID_TO_BIN(UUID());
INSERT INTO `cotizacion` (
        `idCotizacion`,
        `idContrato`,
        `numeroCotizacion`,
        `version`,
        `idEstadoCotizacion`,
        `fechaEmision`,
        `fechaVigenciaDesde`,
        `fechaVigenciaHasta`,
        `observacion`
    )
VALUES (
        @idCot3V1,
        @idContrato3,
        'COT-2024-003',
        1,
        4,
        '2024-09-01',
        '2024-09-01',
        '2025-08-31',
        'Cotización de servicios base'
    ),
    (
        @idCot3V2,
        @idContrato3,
        'COT-2024-004',
        1,
        4,
        '2024-11-01',
        '2024-11-01',
        '2025-08-31',
        'Cotización adicional por expansión de servicios'
    );
-- ============================================
-- 6. DETALLES DE COTIZACIÓN
-- ============================================
-- COT-2024-001 v2 (Vigente) - Mix de servicios en diferentes monedas
INSERT INTO `cotizaciondetalle` (
        `idDetalle`,
        `idCotizacion`,
        `num_item`,
        `idServicio`,
        `idTipoMoneda`,
        `idPeriodicidad`,
        `cantidad`,
        `precioUnitario`,
        `fechaInicioFacturacion`,
        `fechaFinFacturacion`,
        `atributos`,
        `observacion`
    )
VALUES -- Item 1: Instalación (ONE_SHOT en USD)
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        1,
        7,
        1,
        1,
        1,
        500.00,
        '2024-07-01',
        NULL,
        '{"tipo": "instalacion", "specs": {"incluye": ["configuracion_inicial", "capacitacion", "documentacion"]}}',
        'Instalación y configuración inicial del ambiente'
    ),
    -- Item 2: Hosting mensual (MENSUAL en USD)
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        2,
        2,
        1,
        2,
        2,
        150.00,
        '2024-07-01',
        '2024-12-31',
        '{"tipo": "hosting", "specs": {"cpu": 4, "memoria_gb": 16, "disco_gb": 500}}',
        'Hosting Cloud Avanzado - 2 instancias'
    ),
    -- Item 3: Soporte (MENSUAL en CLP)
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        3,
        6,
        2,
        2,
        1,
        500000.00,
        '2024-07-01',
        '2024-12-31',
        '{"tipo": "soporte", "specs": {"horario": "24/7", "sla": "99.9%", "tiempo_respuesta": "15min"}}',
        'Soporte técnico 24/7'
    ),
    -- Item 4: Backup (MENSUAL en UF)
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        4,
        5,
        3,
        2,
        1,
        10.50,
        '2024-07-01',
        '2024-12-31',
        '{"tipo": "backup", "specs": {"capacidad_gb": 2000, "frecuencia": "diaria", "retencion_dias": 30}}',
        'Servicio de backup automático'
    );
-- COT-2024-002 v1 (Vigente) - Servicios cloud
INSERT INTO `cotizaciondetalle` (
        `idDetalle`,
        `idCotizacion`,
        `num_item`,
        `idServicio`,
        `idTipoMoneda`,
        `idPeriodicidad`,
        `cantidad`,
        `precioUnitario`,
        `fechaInicioFacturacion`,
        `fechaFinFacturacion`,
        `atributos`,
        `observacion`
    )
VALUES -- VMs con inicio diferido
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        1,
        4,
        1,
        2,
        5,
        300.00,
        '2024-08-01',
        '2025-05-31',
        '{"tipo": "maquina_virtual", "specs": {"cpu": 8, "memoria_gb": 32, "disco_gb": 1000, "sistema_operativo": "Ubuntu 22.04", "alta_disponibilidad": true}}',
        'Máquinas virtuales premium - Inicio facturación diferido'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        2,
        8,
        1,
        2,
        50,
        12.50,
        '2024-06-01',
        '2025-05-31',
        '{"tipo": "licencia", "specs": {"producto": "Microsoft 365 Business", "usuarios": 50, "incluye": ["Office", "Exchange", "Teams", "OneDrive"]}}',
        'Licencias Microsoft 365'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        3,
        10,
        2,
        2,
        1,
        800000.00,
        '2024-06-01',
        '2025-05-31',
        '{"tipo": "monitoreo", "specs": {"dispositivos": 100, "alertas": "si", "reportes": "mensuales"}}',
        'Monitoreo proactivo de infraestructura'
    );
-- COT-2024-003 v1 (Vigente)
INSERT INTO `cotizaciondetalle` (
        `idDetalle`,
        `idCotizacion`,
        `num_item`,
        `idServicio`,
        `idTipoMoneda`,
        `idPeriodicidad`,
        `cantidad`,
        `precioUnitario`,
        `fechaInicioFacturacion`,
        `fechaFinFacturacion`,
        `atributos`,
        `observacion`
    )
VALUES (
        UUID_TO_BIN(UUID()),
        @idCot3V1,
        1,
        1,
        1,
        2,
        3,
        80.00,
        '2024-09-01',
        '2025-08-31',
        '{"tipo": "hosting", "specs": {"cpu": 2, "memoria_gb": 8, "disco_gb": 250}}',
        'Hosting básico para desarrollo'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot3V1,
        2,
        6,
        2,
        2,
        1,
        300000.00,
        '2024-09-01',
        '2025-08-31',
        '{"tipo": "soporte", "specs": {"horario": "8x5", "sla": "99%"}}',
        'Soporte horario laboral'
    );
-- COT-2024-004 v1 (Vigente - Cotización adicional del mismo contrato)
INSERT INTO `cotizaciondetalle` (
        `idDetalle`,
        `idCotizacion`,
        `num_item`,
        `idServicio`,
        `idTipoMoneda`,
        `idPeriodicidad`,
        `cantidad`,
        `precioUnitario`,
        `fechaInicioFacturacion`,
        `fechaFinFacturacion`,
        `atributos`,
        `observacion`
    )
VALUES (
        UUID_TO_BIN(UUID()),
        @idCot3V2,
        1,
        9,
        1,
        2,
        1,
        450.00,
        '2024-11-01',
        '2025-08-31',
        '{"tipo": "firewall", "specs": {"throughput_gbps": 10, "reglas": 1000, "vpn_tunnels": 50}}',
        'Firewall administrado por expansión'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot3V2,
        2,
        7,
        1,
        1,
        1,
        800.00,
        '2024-11-01',
        NULL,
        '{"tipo": "instalacion", "specs": {"incluye": ["instalacion_firewall", "configuracion_vpn"]}}',
        'Instalación de firewall - pago único'
    );
-- ============================================
-- 7. HISTORIAL DE ESTADOS
-- ============================================
-- Historial de COT-2024-001 v1 (Borrador → Vigente → Reemplazada)
INSERT INTO `cotizacionhistorial` (
        `idHistorial`,
        `idCotizacion`,
        `idEstadoAnterior`,
        `idEstadoNuevo`,
        `comentario`,
        `fechaCambio`
    )
VALUES (
        UUID_TO_BIN(UUID()),
        @idCot1V1,
        NULL,
        1,
        'Cotización creada',
        '2024-01-01 09:00:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V1,
        1,
        2,
        'Enviada a revisión',
        '2024-01-02 10:30:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V1,
        2,
        3,
        'Aprobada por gerencia',
        '2024-01-03 14:15:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V1,
        3,
        4,
        'Activada y vigente',
        '2024-01-05 08:00:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V1,
        4,
        5,
        'Reemplazada por versión 2',
        '2024-06-30 17:00:00'
    );
-- Historial de COT-2024-001 v2 (Borrador → Vigente)
INSERT INTO `cotizacionhistorial` (
        `idHistorial`,
        `idCotizacion`,
        `idEstadoAnterior`,
        `idEstadoNuevo`,
        `comentario`,
        `fechaCambio`
    )
VALUES (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        NULL,
        1,
        'Cotización v2 creada',
        '2024-06-15 09:00:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        1,
        2,
        'Enviada a revisión',
        '2024-06-16 11:00:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        2,
        3,
        'Aprobada con modificaciones',
        '2024-06-20 15:30:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        3,
        4,
        'Activada desde 01/07/2024',
        '2024-07-01 00:01:00'
    );
-- Historial de COT-2024-002 v1
INSERT INTO `cotizacionhistorial` (
        `idHistorial`,
        `idCotizacion`,
        `idEstadoAnterior`,
        `idEstadoNuevo`,
        `comentario`,
        `fechaCambio`
    )
VALUES (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        NULL,
        1,
        'Cotización creada',
        '2024-06-01 09:00:00'
    ),
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        1,
        4,
        'Aprobación express - cliente conocido',
        '2024-06-01 12:00:00'
    );
-- ============================================
-- 8. TOTALES PRECALCULADOS
-- ============================================
-- Totales de COT-2024-001 v2
INSERT INTO `cotizaciontotal` (
        `idCotizacionTotal`,
        `idCotizacion`,
        `idTipoMoneda`,
        `totalOneShot`,
        `totalMensual`,
        `totalAnual`
    )
VALUES -- USD
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        1,
        500.00,
        300.00,
        3600.00
    ),
    -- CLP
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        2,
        0.00,
        500000.00,
        6000000.00
    ),
    -- UF
    (
        UUID_TO_BIN(UUID()),
        @idCot1V2,
        3,
        0.00,
        10.50,
        126.00
    );
-- Totales de COT-2024-002 v1
INSERT INTO `cotizaciontotal` (
        `idCotizacionTotal`,
        `idCotizacion`,
        `idTipoMoneda`,
        `totalOneShot`,
        `totalMensual`,
        `totalAnual`
    )
VALUES -- USD
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        1,
        0.00,
        2125.00,
        25500.00
    ),
    -- CLP
    (
        UUID_TO_BIN(UUID()),
        @idCot2V1,
        2,
        0.00,
        800000.00,
        9600000.00
    );
-- Totales de COT-2024-003 v1
INSERT INTO `cotizaciontotal` (
        `idCotizacionTotal`,
        `idCotizacion`,
        `idTipoMoneda`,
        `totalOneShot`,
        `totalMensual`,
        `totalAnual`
    )
VALUES -- USD
    (
        UUID_TO_BIN(UUID()),
        @idCot3V1,
        1,
        0.00,
        240.00,
        2880.00
    ),
    -- CLP
    (
        UUID_TO_BIN(UUID()),
        @idCot3V1,
        2,
        0.00,
        300000.00,
        3600000.00
    );
-- Totales de COT-2024-004 v1
INSERT INTO `cotizaciontotal` (
        `idCotizacionTotal`,
        `idCotizacion`,
        `idTipoMoneda`,
        `totalOneShot`,
        `totalMensual`,
        `totalAnual`
    )
VALUES -- USD
    (
        UUID_TO_BIN(UUID()),
        @idCot3V2,
        1,
        800.00,
        450.00,
        5400.00
    );
-- ============================================
-- FIN DEL SCRIPT
-- ============================================