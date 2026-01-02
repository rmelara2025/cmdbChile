-- Agregar columna numeroCotizacion si no existe
ALTER TABLE `cotizacion`
ADD COLUMN IF NOT EXISTS `numeroCotizacion` varchar(45) NOT NULL
AFTER `idContrato`;
-- Agregar unique key si no existe
ALTER TABLE `cotizacion`
ADD UNIQUE KEY IF NOT EXISTS `uk_cotizacion_numero_version` (`numeroCotizacion`, `version`);