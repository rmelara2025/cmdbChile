# 🔧 Lista de Tareas de Refactorización - Backend CMDB Chile

**Fecha de análisis:** 23 de enero de 2026  
**Proyecto:** backend-cmdb (Spring Boot 3.3.5, Java 21)

---

## 📋 Resumen Ejecutivo

### Estadísticas
- **System.out.println encontrados:** 13 instancias (CRÍTICO)
- **TODOs encontrados:** 2 instancias
- **Código duplicado:** 5 áreas identificadas
- **Problemas de Clean Code:** 15+ áreas
- **Prioridad ALTA:** 8 tareas
- **Prioridad MEDIA:** 12 tareas
- **Prioridad BAJA:** 6 tareas

---

## 🚨 PRIORIDAD ALTA (Críticas - Resolver Primero)

### 1. ❌ Eliminar System.out.println y usar Logger
**Archivos afectados:**
- `CotizacionService.java` (12 instancias)
- `CodigoGeneradorService.java` (1 instancia)

**Problema:**
```java
// ❌ MAL
System.out.println("Creando nueva cotización para contrato: " + request.getIdContrato());
System.out.println("Código generado: " + codigoCotizacion);
```

**Solución:**
```java
// ✅ BIEN
private static final Logger log = LoggerFactory.getLogger(CotizacionService.class);

log.info("Creando nueva cotización para contrato: {}", request.getIdContrato());
log.debug("Código generado: {}", codigoCotizacion);
```

**Ubicaciones específicas:**
- `CotizacionService.java` líneas: 144, 148, 168, 274, 279, 283, 308, 315, 317, 328, 330, 340
- `CodigoGeneradorService.java` línea: 87

**Impacto:** ALTO - Logs en producción no son trazables, afecta debugging y monitoreo

---

### 2. ❌ Refactorizar Repository con @Query personalizada (TipoMonedaRepository)
**Archivo:** `repository/catalogos/TipoMonedaRepository.java`

**Problema:**
```java
//TODO: refactorizar este metodo, porque perfectamente puedo devolver una lista de la entidad 
// y luego hacer un mapper al response
```

**Solución:**
1. Crear método en Repository que devuelva `List<TipoMoneda>`
2. Crear `TipoMonedaMapper` para mapear a Response
3. Eliminar query nativa con proyecciones manuales

```java
// ❌ MAL (Repository devuelve DTO directamente)
List<TipoMonedaResponse> findAllTipoMonedas();

// ✅ BIEN (Repository devuelve Entity, Service usa Mapper)
List<TipoMoneda> findAll();
```

**Impacto:** MEDIO - Viola separación de responsabilidades (Repository no debe conocer DTOs)

---

### 3. ❌ Eliminar código comentado inútil
**Archivo:** `CotizacionDetalleService.java` línea 128

**Problema:**
```java
// copiar todos los items excepto el que viene en el request
```

**Solución:** Eliminar comentarios obvios o actualizar con información útil

**Impacto:** BAJO - Ruido en el código, dificulta lectura

---

### 4. ⚠️ Manejar Exceptions con clases custom en lugar de RuntimeException genérica
**Archivos afectados:**
- `CodigoGeneradorService.java` (líneas 79, 94)
- `TransicionEstadoService.java` (líneas 45, 68, 72)
- `ContratoService.java` (líneas 23, 45)

**Problema:**
```java
// ❌ MAL
throw new RuntimeException("Error al generar código de cotización: " + e.getMessage(), e);
throw new IllegalStateException("No se encontró transición...");
throw new IllegalArgumentException("La fecha de término debe ser posterior...");
```

**Solución:**
```java
// ✅ BIEN - Crear exceptions específicas
@ResponseStatus(HttpStatus.CONFLICT)
public class CodigoGeneracionException extends RuntimeException {
    public CodigoGeneracionException(String message, Throwable cause) {
        super(message, cause);
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends RuntimeException { ... }

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TransicionEstadoNoPermitidaException extends RuntimeException { ... }
```

**Impacto:** ALTO - Mejora manejo de errores y respuestas HTTP consistentes

---

### 5. 🔄 Extraer lógica de mapeo manual a Mappers dedicados
**Archivos afectados:**
- `CotizacionService.java` (líneas 48-65, 88-110)
- `CotizacionDetalleService.java` (líneas 42-70)

**Problema:**
```java
// ❌ MAL - Mapeo manual en el Service
CotizacionResponse response = new CotizacionResponse();
response.setIdCotizacion(UUID.fromString((String) row[0]));
response.setIdContrato(UUID.fromString((String) row[1]));
response.setNumeroCotizacion((String) row[2]);
// ... 10 líneas más de setters
```

**Solución:**
```java
// ✅ BIEN - Crear CotizacionMapper
@Component
public class CotizacionMapper {
    public CotizacionResponse toResponse(Object[] queryResult) {
        // lógica de mapeo centralizada
    }
}

// En el Service
return cotizacionMapper.toResponse(row);
```

**Impacto:** ALTO - Servicios más limpios, lógica de mapeo reutilizable y testeable

---

### 6. 🧹 Eliminar instanciaciones innecesarias de DTOs en tests
**Archivos afectados:**
- Todos los archivos `*Test.java` en `/test/java/.../service/`

**Problema:**
```java
// ❌ MAL - Instanciación manual repetitiva
ContactoResponse resp = new ContactoResponse(1, saved.getRutCliente(), 
    saved.getTelefono(), saved.getNombre(), saved.getEmail(), null);
```

**Solución:**
```java
// ✅ BIEN - Usar builders o factory methods
ContactoResponse resp = ContactoResponse.builder()
    .id(1)
    .rutCliente(saved.getRutCliente())
    .telefono(saved.getTelefono())
    .build();

// O mejor aún: usar mapper real en tests
ContactoResponse resp = contactoMapper.toResponse(saved);
```

**Impacto:** MEDIO - Tests más mantenibles y cercanos al código real

---

### 7. 📦 Consolidar lógica de construcción de Response Objects
**Archivo:** `CatalogosService.java` (líneas 45, 59, 70, 82, 90, 99)

**Problema:** Múltiples `new XxxResponse()` instanciados manualmente en el service

**Solución:** Crear `CatalogosMapper` con métodos dedicados para cada tipo

**Impacto:** MEDIO - Reduce duplicación y mejora testabilidad

---

### 8. 🎯 Validar que @Transactional esté presente donde se requiere
**Verificar:**
- Todos los métodos que ejecutan `repository.save()`, `repository.delete()`
- Métodos que ejecutan múltiples operaciones que deben ser atómicas

**Archivos a revisar:**
- `CotizacionService.java` ✅ (tiene @Transactional correctamente)
- `ContratoService.java` (verificar)
- `ClienteService.java` (verificar)
- `ContactoServiceImpl.java` (verificar)

**Impacto:** CRÍTICO - Evita inconsistencias de datos

---

## 🟡 PRIORIDAD MEDIA (Mejoras de Clean Code)

### 9. 📝 Agregar JavaDoc a métodos públicos sin documentación
**Archivos afectados:**
- `CotizacionService.java` - algunos métodos tienen, otros no
- `ContratoService.java` - falta documentación completa
- `CatalogosService.java` - sin JavaDoc

**Solución:**
```java
/**
 * Obtiene todas las cotizaciones asociadas a un contrato específico
 * 
 * @param idContrato UUID del contrato
 * @return Lista de cotizaciones con información básica y estado
 * @throws NotFoundException si el contrato no existe
 */
public List<CotizacionResponse> obtenerCotizacionesPorContrato(UUID idContrato) {
    // ...
}
```

**Impacto:** MEDIO - Mejora mantenibilidad y genera documentación auto

---

### 10. 🔢 Reemplazar números mágicos por constantes
**Archivos afectados:**
- `CotizacionService.java` - estados hardcoded (1, 5, etc.)
- `TransicionEstadoService.java` - referencias a IDs de estados

**Problema:**
```java
// ❌ MAL
cotizacion.setIdEstadoCotizacion(1); // BORRADOR
anterior.setIdEstadoCotizacion(5); // REEMPLAZADA
```

**Solución:**
```java
// ✅ BIEN - Crear enum o clase de constantes
public enum EstadoCotizacion {
    BORRADOR(1),
    EN_REVISION(2),
    APROBADA(3),
    VIGENTE(4),
    REEMPLAZADA(5),
    ANULADA(6),
    CANCELADA(7),
    DE_BAJA(8);
    
    private final int id;
    EstadoCotizacion(int id) { this.id = id; }
    public int getId() { return id; }
}

cotizacion.setIdEstadoCotizacion(EstadoCotizacion.BORRADOR.getId());
```

**Impacto:** MEDIO - Código más legible y menos propenso a errores

---

### 11. 🧪 Mejorar cobertura de tests
**Estado actual:**
- Tests básicos existen para algunos servicios
- Falta tests de integración para flujos completos
- No hay tests para Controllers

**Solución:**
1. Agregar tests para todos los métodos públicos de Services
2. Crear tests de integración con `@SpringBootTest`
3. Agregar tests para Controllers con `@WebMvcTest`
4. Configurar JaCoCo para medir cobertura (mínimo 70%)

**Impacto:** ALTO - Previene regresiones y facilita refactorings

---

### 12. 🔐 Centralizar validaciones de negocio
**Problema:** Validaciones mezcladas con lógica de negocio

**Solución:** Crear clases `Validator` dedicadas

```java
@Component
public class ContratoValidator {
    public void validarFechas(LocalDate inicio, LocalDate termino) {
        if (termino.isBefore(inicio)) {
            throw new InvalidDateRangeException("Fecha término debe ser posterior a inicio");
        }
    }
}
```

**Impacto:** MEDIO - Separación de concerns y reutilización de validaciones

---

### 13. 📊 Optimizar queries N+1 en relaciones lazy
**Verificar:**
- `CotizacionDetalleService` cuando carga detalles con relaciones
- `ContratoViewService` con filtros

**Solución:** Usar `@EntityGraph` o `JOIN FETCH` en queries JPQL

**Impacto:** ALTO en rendimiento - Reduce llamadas a BD

---

### 14. 🎨 Estandarizar formato de respuestas de API
**Problema:** Algunas respuestas son objetos directos, otras listas, sin estructura común

**Solución:** Crear wrapper genérico:
```java
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
}
```

**Impacto:** MEDIO - APIs más consistentes y fáciles de consumir

---

### 15. 🏗️ Separar responsabilidades de CotizacionService
**Problema:** `CotizacionService` tiene demasiadas responsabilidades:
- Crear cotizaciones
- Versionar
- Guardar items
- Recalcular totales
- Mapeo de datos

**Solución:** Dividir en:
- `CotizacionService` (CRUD básico)
- `CotizacionVersioningService` (versionado)
- `CotizacionCalculationService` (cálculo de totales)
- `CotizacionMapper` (mapeo)

**Impacto:** ALTO - Mejor adherencia a SRP (Single Responsibility Principle)

---

### 16. 📁 Reorganizar estructura de packages
**Estructura actual:**
```
service/
  ├── CotizacionService.java
  ├── ClienteService.java
  ├── catalogos/
  ├── externos/
  ├── impl/
  └── usuarios/
```

**Estructura propuesta (por feature):**
```
cotizacion/
  ├── service/
  ├── controller/
  ├── repository/
  └── dto/
cliente/
  ├── service/
  ├── controller/
  ...
```

**Impacto:** MEDIO - Mejor cohesión por dominio (DDD)

---

### 17. 🔄 Implementar DTOs de salida separados de entidades
**Problema:** Algunos services devuelven entidades JPA directamente

**Solución:** Siempre mapear a DTO antes de devolver:
```java
// ❌ MAL
public Cotizacion findById(UUID id) {
    return repository.findById(id);
}

// ✅ BIEN
public CotizacionResponse findById(UUID id) {
    Cotizacion cotizacion = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("..."));
    return mapper.toResponse(cotizacion);
}
```

**Impacto:** ALTO - Evita exponer estructura interna de BD

---

### 18. 🌐 Agregar internacionalización (i18n) para mensajes de error
**Solución:** Usar `MessageSource` de Spring

```java
@Component
public class MessageService {
    @Autowired
    private MessageSource messageSource;
    
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
```

**Impacto:** MEDIO - Preparar app para múltiples idiomas

---

### 19. ⚡ Implementar caché para catálogos estáticos
**Archivos afectados:**
- `CatalogosService.java`

**Solución:**
```java
@Cacheable(value = "servicios", unless = "#result.isEmpty()")
public List<ServicioResponse> obtenerServicios() {
    // ...
}
```

**Impacto:** MEDIO - Mejora rendimiento de endpoints frecuentes

---

### 20. 📝 Agregar Swagger/OpenAPI documentation
**Solución:** Configurar SpringDoc

```java
@OpenAPIDefinition(
    info = @Info(
        title = "CMDB Chile API",
        version = "1.0",
        description = "API para gestión de cotizaciones"
    )
)
public class OpenApiConfig { }
```

**Impacto:** ALTO - Documentación automática de API

---

## 🟢 PRIORIDAD BAJA (Nice to Have)

### 21. 🎭 Implementar Aspect para auditoría automática
**Solución:** AOP para loguear todas las operaciones CRUD

```java
@Aspect
@Component
public class AuditAspect {
    @AfterReturning(pointcut = "execution(* com.telefonicatech..service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // log automático
    }
}
```

**Impacto:** BAJO - Mejora trazabilidad sin contaminar código

---

### 22. 🔒 Implementar Rate Limiting
**Solución:** Usar Bucket4j o Redis

**Impacto:** BAJO - Protección contra abuso de API

---

### 23. 📈 Agregar métricas con Actuator y Micrometer
**Solución:** Exponer métricas custom

```java
@Component
public class CotizacionMetrics {
    private final Counter cotizacionesCreadas;
    
    public CotizacionMetrics(MeterRegistry registry) {
        this.cotizacionesCreadas = registry.counter("cotizaciones.creadas");
    }
}
```

**Impacto:** BAJO - Monitoreo avanzado en producción

---

### 24. 🧹 Actualizar dependencias desactualizadas
**Verificar:** `pom.xml` para versiones deprecadas

**Impacto:** BAJO - Seguridad y nuevas features

---

### 25. 🎯 Implementar validación en tiempo de compilación con Bean Validation
**Solución:** Usar `@Valid` consistentemente en todos los Controllers

**Impacto:** BAJO - Ya está implementado en algunos lugares

---

### 26. 📦 Considerar migrar a Records para DTOs inmutables (Java 17+)
**Solución:**
```java
public record CotizacionResponse(
    UUID idCotizacion,
    String numeroCotizacion,
    Integer version
) { }
```

**Impacto:** BAJO - Código más conciso (requiere Java 17+)

---

## 📊 Código Duplicado Identificado

### 1. Mapeo manual de Object[] a DTOs
**Ubicaciones:**
- `CotizacionService.java` (múltiples métodos)
- `CotizacionDetalleService.java`

**Solución:** Centralizar en Mappers

---

### 2. Validación de UUID en Controllers
**Ubicaciones:**
- `CotizacionController.java` línea 67
- Otros controllers

**Solución:** Crear utility method o validador custom

```java
public class UUIDValidator {
    public static UUID validateAndParse(String uuidStr) {
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "UUID inválido: " + uuidStr
            );
        }
    }
}
```

---

### 3. Manejo de errores en Controllers
**Problema:** Bloques try-catch repetidos

**Solución:** Usar `@ControllerAdvice` global

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

---

### 4. Construcción de respuestas en Controllers
**Problema:** Patrón repetido de `ResponseEntity.ok()` y manejo de excepciones

**Solución:** Crear métodos helper o usar `@ControllerAdvice`

---

### 5. Queries nativas similares en Repositories
**Verificar:** Queries con proyecciones manuales repetitivas

**Solución:** Consolidar en métodos reutilizables o usar Specifications

---

## ✅ Checklist de Implementación

### Fase 1 - Crítico (Sprint 1-2 semanas)
- [ ] Reemplazar System.out.println por Logger (Tarea #1)
- [ ] Crear exceptions custom (Tarea #4)
- [ ] Agregar @Transactional donde falte (Tarea #8)
- [ ] Refactorizar TipoMonedaRepository (Tarea #2)

### Fase 2 - Mappers y Clean Code (Sprint 2-3 semanas)
- [ ] Crear Mappers para Cotizacion, CotizacionDetalle, Catalogo (Tarea #5, #7)
- [ ] Reemplazar números mágicos por constantes (Tarea #10)
- [ ] Centralizar validaciones de negocio (Tarea #12)

### Fase 3 - Arquitectura (Sprint 4-6 semanas)
- [ ] Separar responsabilidades de CotizacionService (Tarea #15)
- [ ] Optimizar queries N+1 (Tarea #13)
- [ ] Implementar caché para catálogos (Tarea #19)
- [ ] Estandarizar respuestas API (Tarea #14)

### Fase 4 - Testing y Documentación (Sprint continuo)
- [ ] Mejorar cobertura de tests (Tarea #11)
- [ ] Agregar JavaDoc completo (Tarea #9)
- [ ] Configurar Swagger/OpenAPI (Tarea #20)

### Fase 5 - Mejoras Opcionales (Backlog)
- [ ] Implementar i18n (Tarea #18)
- [ ] Agregar métricas (Tarea #23)
- [ ] Rate limiting (Tarea #22)
- [ ] Auditoría AOP (Tarea #21)

---

## 📌 Notas Importantes

### ✅ Aspectos que YA están bien implementados:
- ✅ Inyección de dependencias por constructor (no usa @Autowired)
- ✅ Uso de DTOs Request/Response
- ✅ Validaciones con Jakarta Bean Validation
- ✅ Transaccionalidad en operaciones críticas
- ✅ Uso de Optional en repositories
- ✅ Separación de concerns Controller → Service → Repository
- ✅ Uso de Lombok para reducir boilerplate
- ✅ Manejo de excepciones con custom exceptions (NotFoundException)

### ⚠️ Riesgos al Refactorizar:
1. **Tests insuficientes:** Refactorizar sin tests puede introducir bugs
2. **Cambio de contratos de API:** Afecta frontend si cambian DTOs
3. **Rendimiento:** Agregar mappers puede impactar performance (usar benchmarks)

### 🎯 Recomendaciones Generales:
1. Refactorizar **una tarea a la vez**
2. Ejecutar tests después de cada cambio
3. Hacer commits pequeños y atómicos
4. Documentar decisiones de diseño en `docs/`
5. Revisar código con otro desarrollador (code review)

---

**Última actualización:** 23 de enero de 2026  
**Autor:** Sistema CMDB Chile - Code Review  
**Versión:** 1.0
