-- ============================================
-- SCRIPT: Creación de tablas del nuevo modelo
-- ============================================
-- Tabla de periodicidades
CREATE TABLE IF NOT EXISTS `periodicidad` (
    `idPeriodicidad` int NOT NULL AUTO_INCREMENT,
    `nombre` varchar(45) NOT NULL,
    `meses` int NOT NULL,
    `descripcion` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`idPeriodicidad`),
    UNIQUE KEY `uk_periodicidad_nombre` (`nombre`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Tabla de estados de cotización
CREATE TABLE IF NOT EXISTS `estadocotizacion` (
    `idEstadoCotizacion` int NOT NULL AUTO_INCREMENT,
    `nombre` varchar(45) NOT NULL,
    `orden` int NOT NULL,
    `descripcion` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`idEstadoCotizacion`),
    UNIQUE KEY `uk_estadocotizacion_nombre` (`nombre`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Tabla de cotización (cabecera)
CREATE TABLE IF NOT EXISTS `cotizacion` (
    `idCotizacion` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
    `idContrato` binary(16) NOT NULL,
    `numeroCotizacion` varchar(45) NOT NULL,
    `version` int NOT NULL DEFAULT 1,
    `idEstadoCotizacion` int NOT NULL,
    `fechaEmision` date NOT NULL,
    `fechaVigenciaDesde` date DEFAULT NULL,
    `fechaVigenciaHasta` date DEFAULT NULL,
    `observacion` varchar(255) DEFAULT NULL,
    `idUsuarioCreacion` varchar(20) DEFAULT NULL,
    `fechaRegistro` datetime DEFAULT CURRENT_TIMESTAMP,
    `fechaModificacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`idCotizacion`),
    UNIQUE KEY `uk_cotizacion_numero_version` (`numeroCotizacion`, `version`),
    KEY `fk_cotizacion_contrato_idx` (`idContrato`),
    KEY `fk_cotizacion_estado_idx` (`idEstadoCotizacion`),
    CONSTRAINT `fk_cotizacion_contrato` FOREIGN KEY (`idContrato`) REFERENCES `contrato` (`idContrato`),
    CONSTRAINT `fk_cotizacion_estadoCotizacion` FOREIGN KEY (`idEstadoCotizacion`) REFERENCES `estadocotizacion` (`idEstadoCotizacion`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Tabla de detalle de cotización (actualizada)
CREATE TABLE IF NOT EXISTS `cotizaciondetalle` (
    `idDetalle` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
    `idCotizacion` binary(16) NOT NULL,
    `num_item` int NOT NULL,
    `idServicio` int NOT NULL,
    `idTipoMoneda` int NOT NULL,
    `idPeriodicidad` int NOT NULL,
    `cantidad` int DEFAULT 1,
    `precioUnitario` decimal(18, 2) NOT NULL,
    `fechaInicioFacturacion` date DEFAULT NULL,
    `fechaFinFacturacion` date DEFAULT NULL,
    `subtotal` decimal(18, 2) GENERATED ALWAYS AS ((`cantidad` * `precioUnitario`)) STORED,
    `atributos` json DEFAULT NULL,
    `observacion` varchar(255) DEFAULT NULL,
    `fechaRegistro` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`idDetalle`),
    KEY `fk_cotizacionDetalle_cotizacion_idx` (`idCotizacion`),
    KEY `fk_cotizacionDetalle_servicio_idx` (`idServicio`),
    KEY `fk_cotizacionDetalle_tipoMoneda_idx` (`idTipoMoneda`),
    KEY `fk_cotizacionDetalle_periodicidad_idx` (`idPeriodicidad`),
    CONSTRAINT `fk_cotizacionDetalle_cotizacion` FOREIGN KEY (`idCotizacion`) REFERENCES `cotizacion` (`idCotizacion`),
    CONSTRAINT `fk_cotizacionDetalle_servicio` FOREIGN KEY (`idServicio`) REFERENCES `servicio` (`idServicio`),
    CONSTRAINT `fk_cotizacionDetalle_tipoMoneda` FOREIGN KEY (`idTipoMoneda`) REFERENCES `tipomoneda` (`idTipoMoneda`),
    CONSTRAINT `fk_cotizacionDetalle_periodicidad` FOREIGN KEY (`idPeriodicidad`) REFERENCES `periodicidad` (`idPeriodicidad`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Tabla de totales por cotización y moneda
CREATE TABLE IF NOT EXISTS `cotizaciontotal` (
    `idCotizacionTotal` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
    `idCotizacion` binary(16) NOT NULL,
    `idTipoMoneda` int NOT NULL,
    `totalOneShot` decimal(18, 2) DEFAULT 0.00,
    `totalMensual` decimal(18, 2) DEFAULT 0.00,
    `totalAnual` decimal(18, 2) DEFAULT 0.00,
    `fechaActualizacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`idCotizacionTotal`),
    UNIQUE KEY `uk_cotizacion_moneda` (`idCotizacion`, `idTipoMoneda`),
    KEY `fk_cotizacionTotal_cotizacion_idx` (`idCotizacion`),
    KEY `fk_cotizacionTotal_tipoMoneda_idx` (`idTipoMoneda`),
    CONSTRAINT `fk_cotizacionTotal_cotizacion` FOREIGN KEY (`idCotizacion`) REFERENCES `cotizacion` (`idCotizacion`) ON DELETE CASCADE,
    CONSTRAINT `fk_cotizacionTotal_tipoMoneda` FOREIGN KEY (`idTipoMoneda`) REFERENCES `tipomoneda` (`idTipoMoneda`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Tabla de historial de cambios de estado
CREATE TABLE IF NOT EXISTS `cotizacionhistorial` (
    `idHistorial` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
    `idCotizacion` binary(16) NOT NULL,
    `idEstadoAnterior` int DEFAULT NULL,
    `idEstadoNuevo` int NOT NULL,
    `idUsuario` varchar(20) DEFAULT NULL,
    `comentario` varchar(500) DEFAULT NULL,
    `motivoRechazo` varchar(500) DEFAULT NULL,
    `fechaCambio` datetime DEFAULT CURRENT_TIMESTAMP,
    `ipAddress` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`idHistorial`),
    KEY `fk_historial_cotizacion_idx` (`idCotizacion`),
    KEY `fk_historial_estadoAnterior_idx` (`idEstadoAnterior`),
    KEY `fk_historial_estadoNuevo_idx` (`idEstadoNuevo`),
    KEY `idx_fecha_cambio` (`fechaCambio`),
    CONSTRAINT `fk_historial_cotizacion` FOREIGN KEY (`idCotizacion`) REFERENCES `cotizacion` (`idCotizacion`) ON DELETE CASCADE,
    CONSTRAINT `fk_historial_estadoAnterior` FOREIGN KEY (`idEstadoAnterior`) REFERENCES `estadocotizacion` (`idEstadoCotizacion`),
    CONSTRAINT `fk_historial_estadoNuevo` FOREIGN KEY (`idEstadoNuevo`) REFERENCES `estadocotizacion` (`idEstadoCotizacion`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;