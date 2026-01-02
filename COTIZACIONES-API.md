# Nuevo Modelo de Cotizaciones

## Cambios Implementados

### 1. Nuevas Entidades JPA

#### `EstadoCotizacion.java`
- Catálogo de estados de cotización (BORRADOR, EN_REVISION, APROBADA, VIGENTE, etc.)

#### `Periodicidad.java`
- Catálogo de periodicidades (ONE_SHOT, MENSUAL, BIMENSUAL, TRIMESTRAL, SEMESTRAL, ANUAL)

#### `Cotizacion.java`
- Tabla intermedia entre `Contrato` y `CotizacionDetalle`
- Permite múltiples cotizaciones por contrato
- Versionamiento de cotizaciones
- Control de estados y vigencias

### 2. Repositorio

#### `CotizacionRepository.java`
- Query nativa con JOIN a `estadocotizacion`
- Fechas formateadas en `dd-MM-yyyy`
- Ordenamiento por número de cotización y versión

### 3. DTOs (siguiendo estándar del proyecto)

#### Request:
- `CotizacionFilterRequest.java` - Para filtros y paginación (preparado para futuras mejoras)

#### Response:
- `CotizacionResponse.java` - Respuesta principal con datos de cotización y estado
- `CotizacionDetalleItemResponse.java` - Items de detalle (preparado para endpoint futuro)
- `CotizacionTotalResponse.java` - Totales por moneda (preparado para endpoint futuro)

### 4. Service y Controller

#### `CotizacionService.java`
- Método `obtenerCotizacionesPorContrato(UUID idContrato)` 
- Mapea Object[] de la query nativa a DTOs

#### `CotizacionController.java`
- Endpoint: `GET /api/contratos/{idContrato}/cotizaciones`
- Validación de UUID
- Manejo de errores HTTP estándar

## Endpoints Disponibles

### 1. Obtener cotizaciones de un contrato (listado)

```
GET /api/contratos/{idContrato}/cotizaciones
```

**Path Parameter:**
- `idContrato` (UUID): Identificador único del contrato

**Response 200 OK:**
```json
[
  {
    "idCotizacion": "uuid",
    "idContrato": "uuid",
    "numeroCotizacion": "COT-2024-001",
    "version": 2,
    "estadoNombre": "VIGENTE",
    "estadoDescripcion": "Cotización actualmente vigente",
    "fechaEmision": "01-07-2024",
    "fechaVigenciaDesde": "01-07-2024",
    "fechaVigenciaHasta": "31-12-2024",
    "observacion": "Versión actualizada con nuevos servicios",
    "fechaRegistro": "15-06-2024 09:00:00"
  }
]
```

**Response 400 Bad Request:**
```json
{
  "status": 400,
  "message": "idContrato debe ser un UUID válido"
}
```

---

### 2. Obtener detalle completo de una cotización

```
GET /api/cotizaciones/{idCotizacion}
```

**Path Parameter:**
- `idCotizacion` (UUID): Identificador único de la cotización

**Response 200 OK:**
```json
{
  "idCotizacion": "uuid",
  "idContrato": "uuid",
  "numeroCotizacion": "COT-2024-001",
  "version": 2,
  "estadoNombre": "VIGENTE",
  "estadoDescripcion": "Cotización actualmente vigente",
  "fechaEmision": "01-07-2024",
  "fechaVigenciaDesde": "01-07-2024",
  "fechaVigenciaHasta": "31-12-2024",
  "observacion": "Versión actualizada con nuevos servicios",
  "fechaRegistro": "15-06-2024 09:00:00",
  "detalles": [
    {
      "idDetalle": "uuid",
      "numItem": 1,
      "idServicio": 7,
      "nombreServicio": "Instalación y Configuración",
      "nombreFamilia": "Servicios Profesionales",
      "cantidad": 1,
      "precioUnitario": 500.00,
      "subtotal": 500.00,
      "nombreTipoMoneda": "Dólar",
      "periodicidad": "ONE_SHOT",
      "fechaInicioFacturacion": "01-07-2024",
      "fechaFinFacturacion": null,
      "atributos": "{\"tipo\": \"instalacion\", \"specs\": {...}}",
      "observacion": "Instalación inicial"
    },
    {
      "idDetalle": "uuid",
      "numItem": 2,
      "idServicio": 2,
      "nombreServicio": "Hosting Cloud Avanzado",
      "nombreFamilia": "Cloud Services",
      "cantidad": 2,
      "precioUnitario": 150.00,
      "subtotal": 300.00,
      "nombreTipoMoneda": "Dólar",
      "periodicidad": "MENSUAL",
      "fechaInicioFacturacion": "01-07-2024",
      "fechaFinFacturacion": "31-12-2024",
      "atributos": "{\"tipo\": \"hosting\", \"specs\": {...}}",
      "observacion": "2 instancias"
    }
  ],
  "totales": [
    {
      "nombreMoneda": "Peso Chileno",
      "codigoMoneda": "CLP",
      "totalOneShot": 0.00,
      "totalMensual": 500000.00,
      "totalAnual": 6000000.00
    },
    {
      "nombreMoneda": "Dólar",
      "codigoMoneda": "USD",
      "totalOneShot": 500.00,
      "totalMensual": 300.00,
      "totalAnual": 3600.00
    }
  ]
}
```

**Response 404 Not Found:**
```json
{
  "status": 404,
  "message": "Cotización no encontrada con id: {uuid}"
}
```

**Response 400 Bad Request:**
```json
{
  "status": 400,
  "message": "idCotizacion debe ser un UUID válido"
}
```

## Migraciones Necesarias

### Ejecutar scripts SQL:
1. `src/main/resources/db/01-schema.sql` - Crear estructura de tablas
2. `src/main/resources/db/02-data-dummy.sql` - Datos de prueba

## Próximos Pasos Sugeridos

### 1. Endpoint para obtener detalles de una cotización específica
```
GET /api/cotizaciones/{idCotizacion}/detalles
```

### 2. Endpoint para obtener totales de una cotización
```
GET /api/cotizaciones/{idCotizacion}/totales
```

### 3. Actualizar `CotizacionDetalleRepository`
- Modificar queries para usar `idCotizacion` en lugar de `idContrato`
- Eliminar lógica de versionamiento manual (ahora está en tabla `cotizacion`)

### 4. Endpoints de gestión de cotizaciones
- `POST /api/contratos/{idContrato}/cotizaciones` - Crear nueva cotización
- `PUT /api/cotizaciones/{idCotizacion}` - Actualizar cotización
- `POST /api/cotizaciones/{idCotizacion}/versionar` - Crear nueva versión
- `PUT /api/cotizaciones/{idCotizacion}/cambiar-estado` - Cambiar estado

### 5. Endpoints de gestión de detalles
- `POST /api/cotizaciones/{idCotizacion}/detalles` - Agregar item
- `PUT /api/cotizaciones/detalles/{idDetalle}` - Editar item
- `DELETE /api/cotizaciones/detalles/{idDetalle}` - Eliminar item

## Notas Importantes

- Las fechas en la BD están en formato ISO (yyyy-MM-dd) pero se devuelven en formato chileno (dd-MM-yyyy)
- El mapeo de Object[] a DTO es manual para tener control total sobre los tipos
- El controller valida que el UUID sea válido antes de llamar al service
- Se mantiene el estándar del proyecto: Request/Response DTOs, no se exponen entidades al frontend
