package microservice.reporte.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import microservice.reporte.model.ExportacionReporte;
import microservice.reporte.model.Metrica;
import microservice.reporte.model.ReporteInventario;
import microservice.reporte.model.ReporteSucursal;
import microservice.reporte.model.ReporteVentas;
import microservice.reporte.model.Reportes;
import microservice.reporte.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService reporteService;

    @Test
    void postReporteDebeCrearReporte() throws Exception {
        when(reporteService.crearReportes(any(Reportes.class))).thenReturn(crearReporte());

        mockMvc.perform(post("/api/v1/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReporte()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Reporte mensual"));
    }

    @Test
    void getReportesDebeRetornarLista() throws Exception {
        when(reporteService.obtenerReportes()).thenReturn(List.of(crearReporte()));

        mockMvc.perform(get("/api/v1/reportes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].tipo").value("General"));
    }

    @Test
    void putReportesDebeActualizarReporte() throws Exception {
        Reportes actualizado = crearReporte();
        actualizado.setTitulo("Reporte actualizado");
        when(reporteService.updateReportes(eq(1L), any(Reportes.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/reportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReporte()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Reporte actualizado"));
    }

    @Test
    void deleteReporteDebeEliminarReporte() throws Exception {
        mockMvc.perform(delete("/api/v1/reportes/1"))
            .andExpect(status().isOk());

        verify(reporteService).eliminarReporte(1L);
    }

    @Test
    void postReporteVentasDebeGenerarReporte() throws Exception {
        when(reporteService.generarReporteVentas(any(ReporteVentas.class))).thenReturn(crearReporteVentas());

        mockMvc.perform(post("/api/v1/reportes/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReporteVentas()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipo").value("Ventas"));
    }

    @Test
    void getReportesVentasDebeRetornarLista() throws Exception {
        when(reporteService.obtenerReportesVentas()).thenReturn(List.of(crearReporteVentas()));

        mockMvc.perform(get("/api/v1/reportes/ventas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].periodo").value("2026-06"));
    }

    @Test
    void postReporteInventarioDebeGenerarReporte() throws Exception {
        when(reporteService.generarReporteInventario(any(ReporteInventario.class))).thenReturn(crearReporteInventario());

        mockMvc.perform(post("/api/v1/reportes/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReporteInventario()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipo").value("Inventario"));
    }

    @Test
    void getReportesInventarioDebeRetornarLista() throws Exception {
        when(reporteService.obtenerReportesInventario()).thenReturn(List.of(crearReporteInventario()));

        mockMvc.perform(get("/api/v1/reportes/inventario"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].totalProductos").value(100));
    }

    @Test
    void postReporteSucursalDebeGenerarReporte() throws Exception {
        when(reporteService.generarReporteSucursal(any(ReporteSucursal.class))).thenReturn(crearReporteSucursal());

        mockMvc.perform(post("/api/v1/reportes/sucursal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReporteSucursal()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipo").value("Sucursal"));
    }

    @Test
    void getReportesSucursalDebeRetornarLista() throws Exception {
        when(reporteService.obtenerReportesSucursal()).thenReturn(List.of(crearReporteSucursal()));

        mockMvc.perform(get("/api/v1/reportes/sucursal"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombreSucursal").value("Sucursal Centro"));
    }

    @Test
    void postMetricaDebeAgregarMetrica() throws Exception {
        when(reporteService.agregarMetrica(eq(1L), any(Metrica.class))).thenReturn(crearMetrica());

        mockMvc.perform(post("/api/v1/reportes/1/metricas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMetrica()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Ventas"));
    }

    @Test
    void getMetricasDebeRetornarLista() throws Exception {
        when(reporteService.obtenerMetricasPorReporte(1L)).thenReturn(List.of(crearMetrica()));

        mockMvc.perform(get("/api/v1/reportes/1/metricas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].unidad").value("CLP"));
    }

    @Test
    void postExportacionDebeExportarReporte() throws Exception {
        when(reporteService.exportarReporte(eq(1L), any(ExportacionReporte.class))).thenReturn(crearExportacion());

        mockMvc.perform(post("/api/v1/reportes/1/exportaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonExportacion()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.formato").value("PDF"));
    }

    @Test
    void getExportacionesDebeRetornarLista() throws Exception {
        when(reporteService.obtenerExportacionesPorReporte(1L)).thenReturn(List.of(crearExportacion()));

        mockMvc.perform(get("/api/v1/reportes/1/exportaciones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].estado").value("Exportado"));
    }

    private Reportes crearReporte() {
        Reportes reporte = new Reportes();
        completarBase(reporte, "General");
        reporte.setIdReporte(1L);
        return reporte;
    }

    private ReporteVentas crearReporteVentas() {
        ReporteVentas reporte = new ReporteVentas();
        completarBase(reporte, "Ventas");
        reporte.setTotalVentas(150000);
        reporte.setCantidadVentas(12);
        reporte.setPeriodo("2026-06");
        reporte.setIdSucursal(1);
        return reporte;
    }

    private ReporteInventario crearReporteInventario() {
        ReporteInventario reporte = new ReporteInventario();
        completarBase(reporte, "Inventario");
        reporte.setTotalProductos(100);
        reporte.setStockBajo(8);
        reporte.setMovimientos(20);
        return reporte;
    }

    private ReporteSucursal crearReporteSucursal() {
        ReporteSucursal reporte = new ReporteSucursal();
        completarBase(reporte, "Sucursal");
        reporte.setIdSucursal(1);
        reporte.setNombreSucursal("Sucursal Centro");
        reporte.setVentasSucursal(250000);
        reporte.setRendimiento(92.5);
        return reporte;
    }

    private void completarBase(Reportes reporte, String tipo) {
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Generado");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo(tipo);
        reporte.setFormato("JSON");
    }

    private Metrica crearMetrica() {
        Metrica metrica = new Metrica();
        metrica.setNombre("Ventas");
        metrica.setValor(120.5);
        metrica.setUnidad("CLP");
        return metrica;
    }

    private ExportacionReporte crearExportacion() {
        ExportacionReporte exportacion = new ExportacionReporte();
        exportacion.setFormato("PDF");
        exportacion.setEstado("Exportado");
        exportacion.setRutaArchivo("reportes/1.pdf");
        return exportacion;
    }

    private String jsonReporte() {
        return """
            {
              "fechaReporte": "2026-06-12",
              "razonReporte": "Reporte mensual",
              "descripcionReporte": "Revision de indicadores",
              "estadoReporte": "Pendiente",
              "titulo": "Reporte mensual",
              "tipo": "General",
              "formato": "JSON"
            }
            """;
    }

    private String jsonReporteVentas() {
        return """
            {
              "fechaReporte": "2026-06-12",
              "razonReporte": "Ventas",
              "descripcionReporte": "Revision de ventas",
              "estadoReporte": "Pendiente",
              "titulo": "Reporte ventas",
              "tipo": "Ventas",
              "formato": "JSON",
              "totalVentas": 150000,
              "cantidadVentas": 12,
              "periodo": "2026-06",
              "idSucursal": 1
            }
            """;
    }

    private String jsonReporteInventario() {
        return """
            {
              "fechaReporte": "2026-06-12",
              "razonReporte": "Inventario",
              "descripcionReporte": "Revision de inventario",
              "estadoReporte": "Pendiente",
              "titulo": "Reporte inventario",
              "tipo": "Inventario",
              "formato": "JSON",
              "totalProductos": 100,
              "stockBajo": 8,
              "movimientos": 20
            }
            """;
    }

    private String jsonReporteSucursal() {
        return """
            {
              "fechaReporte": "2026-06-12",
              "razonReporte": "Sucursal",
              "descripcionReporte": "Revision de sucursal",
              "estadoReporte": "Pendiente",
              "titulo": "Reporte sucursal",
              "tipo": "Sucursal",
              "formato": "JSON",
              "idSucursal": 1,
              "nombreSucursal": "Sucursal Centro",
              "ventasSucursal": 250000,
              "rendimiento": 92.5
            }
            """;
    }

    private String jsonMetrica() {
        return """
            {
              "nombre": "Ventas",
              "valor": 120.5,
              "unidad": "CLP"
            }
            """;
    }

    private String jsonExportacion() {
        return """
            {
              "formato": "PDF",
              "estado": "Pendiente"
            }
            """;
    }
}
