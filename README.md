# Microservicio Reportes - Pruebas Unitarias

Este repositorio contiene un microservicio Spring Boot para la gestion de reportes. La funcionalidad principal esta organizada en una entidad JPA, un repositorio, un servicio y un controlador REST.

El diagrama de clases adjunto representa el dominio de reportes y sus responsabilidades principales. En el codigo actual, estas responsabilidades se concentran en la clase `Reportes` y en las capas `ReporteController`, `ReporteService` y `ReporteRepository`.

## Estructura Del Proyecto

```text
src/main/java/microservice/soporte
|-- controller/ReporteController.java
|-- model/Reportes.java
|-- repository/ReporteRepository.java
|-- service/ReporteService.java
|-- SoporteApplication.java

src/test/java/microservice/soporte
|-- SoporteApplicationTests.java
```

## Clases Principales Implementadas

### `Reportes`

Entidad JPA que representa un reporte registrado en la base de datos. Sus campos principales son:

- `idReporte`
- `fechaReporte`
- `razonReporte`
- `descripcionReporte`
- `estadoReporte`
- `titulo`
- `tipo`
- `formato`

Tambien contiene metodos base del diagrama:

- `generar`
- `exportar`
- `visualizar`

Los campos historicos del CRUD mantienen validaciones con `@NotBlank`. Los campos `titulo`, `tipo` y `formato` se completan con valores por defecto si no llegan en la peticion, para mantener compatibilidad con el CRUD original.

### `ReporteVentas`

Especializacion de `Reportes` para reportes de ventas.

Campos principales:

- `totalVentas`
- `cantidadVentas`
- `periodo`
- `idSucursal`

Metodos:

- `generarReporteVentas`
- `calcularTotales`

### `ReporteInventario`

Especializacion de `Reportes` para reportes de inventario.

Campos principales:

- `totalProductos`
- `stockBajo`
- `movimientos`
- `fechaCorte`

Metodos:

- `generarReporteInventario`
- `calcularStock`

### `ReporteSucursal`

Especializacion de `Reportes` para reportes por sucursal.

Campos principales:

- `idSucursal`
- `nombreSucursal`
- `ventasSucursal`
- `rendimiento`

Metodos:

- `generarReporteSucursal`
- `compararMetricas`

### `Metrica`

Entidad relacionada con un reporte. Permite registrar indicadores asociados al reporte generado.

Campos principales:

- `idMetrica`
- `nombre`
- `valor`
- `unidad`
- `fechaRegistro`

Metodos:

- `calcular`
- `visualizar`

### `ExportacionReporte`

Entidad relacionada con un reporte. Permite registrar exportaciones del reporte.

Campos principales:

- `idExportacion`
- `formato`
- `rutaArchivo`
- `fechaExportacion`
- `estado`

Metodos:

- `exportarExcel`
- `exportarPDF`
- `descargar`

### `ReporteRepository`

Interfaz que extiende `JpaRepository<Reportes, Long>`. Permite usar operaciones CRUD como:

- `save`
- `findAll`
- `findById`
- `deleteById`

### `ReporteService`

Capa encargada de la logica de negocio. Sus metodos principales son:

- `crearReportes`
- `obtenerReportes`
- `obtenerReportePorId`
- `updateReportes`
- `eliminarReporte`

### `ReporteController`

Controlador REST expuesto en la ruta:

```text
api/v1/reportes
```

Endpoints disponibles:

| Metodo | Ruta | Accion |
| --- | --- | --- |
| `POST` | `/api/v1/reportes` | Crear reporte |
| `GET` | `/api/v1/reportes` | Listar reportes |
| `PUT` | `/api/v1/reportes/{id}` | Actualizar reporte |
| `DELETE` | `/api/v1/reportes/{id}` | Eliminar reporte |

Endpoints agregados desde el diagrama:

| Metodo | Ruta | Accion |
| --- | --- | --- |
| `POST` | `/api/v1/reportes/ventas` | Generar reporte de ventas |
| `GET` | `/api/v1/reportes/ventas` | Listar reportes de ventas |
| `POST` | `/api/v1/reportes/inventario` | Generar reporte de inventario |
| `GET` | `/api/v1/reportes/inventario` | Listar reportes de inventario |
| `POST` | `/api/v1/reportes/sucursal` | Generar reporte de sucursal |
| `GET` | `/api/v1/reportes/sucursal` | Listar reportes de sucursal |
| `POST` | `/api/v1/reportes/{id}/metricas` | Agregar metrica a un reporte |
| `GET` | `/api/v1/reportes/{id}/metricas` | Listar metricas de un reporte |
| `POST` | `/api/v1/reportes/{id}/exportaciones` | Exportar un reporte |
| `GET` | `/api/v1/reportes/{id}/exportaciones` | Listar exportaciones de un reporte |

## Objetivo De Las Pruebas Unitarias

Las pruebas unitarias permiten verificar cada componente de forma aislada, evitando depender directamente de la base de datos MySQL o de otros servicios externos.

En este microservicio, las pruebas unitarias deben validar principalmente:

- Que el servicio invoque correctamente al repositorio.
- Que la actualizacion modifique los datos esperados.
- Que la eliminacion use el identificador correcto.
- Que el controlador entregue las solicitudes a la capa de servicio.
- Que las validaciones del modelo rechacen campos obligatorios vacios.

## Pruebas Sugeridas Para `ReporteService`

La clase `ReporteService` debe probarse usando mocks de `ReporteRepository`.

Casos recomendados:

| Metodo | Caso De Prueba | Resultado Esperado |
| --- | --- | --- |
| `crearReportes` | Guardar un reporte valido | Retorna el reporte guardado |
| `obtenerReportes` | Consultar todos los reportes | Retorna una lista de reportes |
| `obtenerReportePorId` | Buscar un id existente | Retorna el reporte encontrado |
| `obtenerReportePorId` | Buscar un id inexistente | Retorna `null` |
| `updateReportes` | Actualizar un reporte existente | Actualiza fecha, razon, descripcion y estado |
| `eliminarReporte` | Eliminar por id | Ejecuta `deleteById` con el id recibido |

Ejemplo de enfoque con Mockito:

```java
@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void crearReportesDebeGuardarReporte() {
        Reportes reporte = new Reportes(
            1L,
            "2026-06-12",
            "Falla de sistema",
            "Error al generar reporte",
            "Pendiente"
        );

        when(reporteRepository.save(reporte)).thenReturn(reporte);

        Reportes resultado = reporteService.crearReportes(reporte);

        assertEquals("Falla de sistema", resultado.getRazonReporte());
        verify(reporteRepository).save(reporte);
    }
}
```

## Pruebas Sugeridas Para `ReporteController`

El controlador puede probarse con `MockMvc` para validar que los endpoints respondan correctamente sin levantar toda la aplicacion.

Casos recomendados:

| Endpoint | Caso De Prueba | Resultado Esperado |
| --- | --- | --- |
| `POST /api/v1/reportes` | Crear reporte con datos validos | Respuesta correcta con el reporte creado |
| `GET /api/v1/reportes` | Listar reportes existentes | Retorna arreglo JSON |
| `PUT /api/v1/reportes/{id}` | Actualizar reporte existente | Retorna reporte actualizado |
| `DELETE /api/v1/reportes/{id}` | Eliminar reporte | Respuesta sin error |

Ejemplo de enfoque:

```java
@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void getReportesDebeRetornarLista() throws Exception {
        when(reporteService.obtenerReportes()).thenReturn(List.of(
            new Reportes(1L, "2026-06-12", "Inventario", "Revision mensual", "Generado")
        ));

        mockMvc.perform(get("/api/v1/reportes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].razonReporte").value("Inventario"));
    }
}
```

## Pruebas Sugeridas Para `Reportes`

La entidad debe probarse verificando sus validaciones obligatorias.

Casos recomendados:

- `fechaReporte` no debe estar vacio.
- `razonReporte` no debe estar vacio.
- `descripcionReporte` no debe estar vacio.
- `estadoReporte` no debe estar vacio.

Estas pruebas ayudan a asegurar que los datos minimos del reporte sean enviados antes de guardarlos.

## Comando Para Ejecutar Las Pruebas

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

## Estado Actual De Pruebas

Actualmente el proyecto incluye la clase:

```text
src/test/java/microservice/soporte/SoporteApplicationTests.java
src/test/java/microservice/soporte/service/ReporteServiceTest.java
src/test/resources/application-test.properties
```

`SoporteApplicationTests` verifica que el contexto de Spring Boot cargue correctamente usando el perfil `test` con base de datos H2 en memoria, por lo que las pruebas no dependen de MySQL local.

```java
@SpringBootTest
@ActiveProfiles("test")
class SoporteApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

`ReporteServiceTest` valida casos unitarios con Mockito:

- creacion de reportes;
- busqueda por id inexistente;
- actualizacion de campos base y campos del diagrama.

Para mejorar todavia mas la cobertura, se recomienda agregar pruebas especificas para:

- `ReporteControllerTest`
- `ReportesValidationTest`
- `ReporteVentasTest`
- `ReporteInventarioTest`
- `ReporteSucursalTest`

## Relacion Con El Diagrama De Clases

El diagrama adjunto describe un microservicio de reportes con una clase base `Reporte` y especializaciones como reportes de ventas, inventario y sucursal. Tambien incluye clases relacionadas con metricas y exportacion.

El repositorio ahora implementa una version inicial del dominio del diagrama mediante:

- una entidad base `Reportes`;
- reportes especializados de ventas, inventario y sucursal;
- metricas asociadas a reportes;
- exportaciones asociadas a reportes;
- endpoints para generar, consultar, metrificar y exportar reportes.

## Buenas Practicas Para Las Unitarias

- Probar una responsabilidad por prueba.
- Usar nombres descriptivos en los metodos de prueba.
- Simular dependencias externas con Mockito.
- Evitar depender de MySQL en pruebas unitarias.
- Mantener los datos de prueba simples y claros.
- Agregar pruebas nuevas cada vez que se agregue una regla de negocio.
