package microservice.soporte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import microservice.soporte.model.ExportacionReporte;
import microservice.soporte.model.Metrica;
import microservice.soporte.model.ReporteInventario;
import microservice.soporte.model.ReporteSucursal;
import microservice.soporte.model.ReporteVentas;
import microservice.soporte.model.Reportes;
import microservice.soporte.repository.ExportacionReporteRepository;
import microservice.soporte.repository.MetricaRepository;
import microservice.soporte.repository.ReporteInventarioRepository;
import microservice.soporte.repository.ReporteRepository;
import microservice.soporte.repository.ReporteSucursalRepository;
import microservice.soporte.repository.ReporteVentasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteVentasRepository reporteVentasRepository;

    @Mock
    private ReporteInventarioRepository reporteInventarioRepository;

    @Mock
    private ReporteSucursalRepository reporteSucursalRepository;

    @Mock
    private MetricaRepository metricaRepository;

    @Mock
    private ExportacionReporteRepository exportacionReporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void crearReportesDebeGuardarReporte() {
        Reportes reporte = crearReporte();
        when(reporteRepository.save(reporte)).thenReturn(reporte);

        Reportes resultado = reporteService.crearReportes(reporte);

        assertEquals("Reporte mensual", resultado.getTitulo());
        verify(reporteRepository).save(reporte);
    }

    @Test
    void obtenerReportesDebeRetornarLista() {
        when(reporteRepository.findAll()).thenReturn(List.of(crearReporte()));

        List<Reportes> resultado = reporteService.obtenerReportes();

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerReportePorIdDebeRetornarReporteSiExiste() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(crearReporte()));

        Reportes resultado = reporteService.obtenerReportePorId(1L);

        assertEquals(1L, resultado.getIdReporte());
    }

    @Test
    void obtenerReportePorIdDebeRetornarNullSiNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Reportes resultado = reporteService.obtenerReportePorId(99L);

        assertNull(resultado);
    }

    @Test
    void updateReportesDebeActualizarCamposDelDiagrama() {
        Reportes existente = crearReporte();
        Reportes cambios = crearReporte();
        cambios.setTitulo("Reporte actualizado");
        cambios.setTipo("Sucursal");
        cambios.setFormato("PDF");
        cambios.setEstadoReporte("Generado");

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reporteRepository.save(existente)).thenReturn(existente);

        Reportes resultado = reporteService.updateReportes(1L, cambios);

        assertEquals("Reporte actualizado", resultado.getTitulo());
        assertEquals("Sucursal", resultado.getTipo());
        assertEquals("PDF", resultado.getFormato());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void updateReportesDebeRetornarNullSiNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Reportes resultado = reporteService.updateReportes(99L, crearReporte());

        assertNull(resultado);
    }

    @Test
    void eliminarReporteDebeEliminarPorId() {
        reporteService.eliminarReporte(1L);

        verify(reporteRepository).deleteById(1L);
    }

    @Test
    void generarReporteVentasDebeMarcarTipoYEstado() {
        ReporteVentas reporte = crearReporteVentas();
        when(reporteVentasRepository.save(reporte)).thenReturn(reporte);

        ReporteVentas resultado = reporteService.generarReporteVentas(reporte);

        assertEquals("Ventas", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void generarReporteInventarioDebeMarcarTipoYEstado() {
        ReporteInventario reporte = crearReporteInventario();
        when(reporteInventarioRepository.save(reporte)).thenReturn(reporte);

        ReporteInventario resultado = reporteService.generarReporteInventario(reporte);

        assertEquals("Inventario", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void generarReporteSucursalDebeMarcarTipoYEstado() {
        ReporteSucursal reporte = crearReporteSucursal();
        when(reporteSucursalRepository.save(reporte)).thenReturn(reporte);

        ReporteSucursal resultado = reporteService.generarReporteSucursal(reporte);

        assertEquals("Sucursal", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void agregarMetricaDebeAsociarlaAlReporte() {
        Metrica metrica = crearMetrica();
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(crearReporte()));
        when(metricaRepository.save(any(Metrica.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Metrica resultado = reporteService.agregarMetrica(1L, metrica);

        assertNotNull(resultado.getReporte());
        assertNotNull(resultado.getFechaRegistro());
        verify(metricaRepository).save(metrica);
    }

    @Test
    void agregarMetricaDebeRetornarNullSiReporteNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Metrica resultado = reporteService.agregarMetrica(99L, crearMetrica());

        assertNull(resultado);
    }

    @Test
    void obtenerMetricasPorReporteDebeConsultarRepositorio() {
        when(metricaRepository.findByReporteIdReporte(1L)).thenReturn(List.of(crearMetrica()));

        List<Metrica> resultado = reporteService.obtenerMetricasPorReporte(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void exportarReporteDebeActualizarEstadoYCrearExportacion() {
        ExportacionReporte exportacion = crearExportacion();
        Reportes reporte = crearReporte();
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(exportacionReporteRepository.save(any(ExportacionReporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExportacionReporte resultado = reporteService.exportarReporte(1L, exportacion);

        assertEquals("Exportado", resultado.getEstado());
        assertEquals("Exportado", reporte.getEstadoReporte());
        assertEquals("reportes/1.pdf", resultado.getRutaArchivo());
        assertNotNull(resultado.getFechaExportacion());
    }

    @Test
    void exportarReporteDebeRetornarNullSiReporteNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        ExportacionReporte resultado = reporteService.exportarReporte(99L, crearExportacion());

        assertNull(resultado);
    }

    private Reportes crearReporte() {
        Reportes reporte = new Reportes();
        reporte.setIdReporte(1L);
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo("General");
        reporte.setFormato("JSON");
        return reporte;
    }

    private ReporteVentas crearReporteVentas() {
        ReporteVentas reporte = new ReporteVentas();
        completarBase(reporte);
        reporte.setTotalVentas(150000);
        reporte.setCantidadVentas(12);
        reporte.setPeriodo("2026-06");
        reporte.setIdSucursal(1);
        return reporte;
    }

    private ReporteInventario crearReporteInventario() {
        ReporteInventario reporte = new ReporteInventario();
        completarBase(reporte);
        reporte.setTotalProductos(100);
        reporte.setStockBajo(8);
        reporte.setMovimientos(20);
        return reporte;
    }

    private ReporteSucursal crearReporteSucursal() {
        ReporteSucursal reporte = new ReporteSucursal();
        completarBase(reporte);
        reporte.setIdSucursal(1);
        reporte.setNombreSucursal("Sucursal Centro");
        reporte.setVentasSucursal(250000);
        reporte.setRendimiento(92.5);
        return reporte;
    }

    private void completarBase(Reportes reporte) {
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo("General");
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
        exportacion.setEstado("Pendiente");
        return exportacion;
    }
}
