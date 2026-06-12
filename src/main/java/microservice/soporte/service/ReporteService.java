package microservice.soporte.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

@Service
@Transactional
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private ReporteVentasRepository reporteVentasRepository;

    @Autowired
    private ReporteInventarioRepository reporteInventarioRepository;

    @Autowired
    private ReporteSucursalRepository reporteSucursalRepository;

    @Autowired
    private MetricaRepository metricaRepository;

    @Autowired
    private ExportacionReporteRepository exportacionReporteRepository;

    public Reportes crearReportes(Reportes reporte){
        return reporteRepository.save(reporte);
    }

    public List<Reportes> obtenerReportes() {
        return reporteRepository.findAll();
    }

    public Reportes obtenerReportePorId(Long id) {
        return reporteRepository.findById(id).orElse(null);
    }

    public Reportes updateReportes(Long id, Reportes reportes) {
        Reportes reporteExistente = reporteRepository.findById(id).orElse(null);
        if (reporteExistente != null) {
            reporteExistente.setFechaReporte(reportes.getFechaReporte());
            reporteExistente.setRazonReporte(reportes.getRazonReporte());
            reporteExistente.setDescripcionReporte(reportes.getDescripcionReporte());
            reporteExistente.setEstadoReporte(reportes.getEstadoReporte());
            reporteExistente.setTitulo(reportes.getTitulo());
            reporteExistente.setTipo(reportes.getTipo());
            reporteExistente.setFormato(reportes.getFormato());
            return reporteRepository.save(reporteExistente);
        }
        return null;
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public ReporteVentas generarReporteVentas(ReporteVentas reporteVentas) {
        reporteVentas.generarReporteVentas();
        return reporteVentasRepository.save(reporteVentas);
    }

    public List<ReporteVentas> obtenerReportesVentas() {
        return reporteVentasRepository.findAll();
    }

    public ReporteInventario generarReporteInventario(ReporteInventario reporteInventario) {
        reporteInventario.generarReporteInventario();
        return reporteInventarioRepository.save(reporteInventario);
    }

    public List<ReporteInventario> obtenerReportesInventario() {
        return reporteInventarioRepository.findAll();
    }

    public ReporteSucursal generarReporteSucursal(ReporteSucursal reporteSucursal) {
        reporteSucursal.generarReporteSucursal();
        return reporteSucursalRepository.save(reporteSucursal);
    }

    public List<ReporteSucursal> obtenerReportesSucursal() {
        return reporteSucursalRepository.findAll();
    }

    public Metrica agregarMetrica(Long idReporte, Metrica metrica) {
        Reportes reporte = obtenerReportePorId(idReporte);
        if (reporte == null) {
            return null;
        }
        metrica.setReporte(reporte);
        metrica.setFechaRegistro(LocalDateTime.now());
        return metricaRepository.save(metrica);
    }

    public List<Metrica> obtenerMetricasPorReporte(Long idReporte) {
        return metricaRepository.findByReporteIdReporte(idReporte);
    }

    public ExportacionReporte exportarReporte(Long idReporte, ExportacionReporte exportacionReporte) {
        Reportes reporte = obtenerReportePorId(idReporte);
        if (reporte == null) {
            return null;
        }

        exportacionReporte.setReporte(reporte);
        exportacionReporte.setFechaExportacion(LocalDateTime.now());
        exportacionReporte.setEstado("Exportado");
        if (exportacionReporte.getRutaArchivo() == null || exportacionReporte.getRutaArchivo().isBlank()) {
            exportacionReporte.setRutaArchivo("reportes/" + idReporte + "." + exportacionReporte.getFormato().toLowerCase());
        }

        reporte.exportar();
        reporteRepository.save(reporte);
        return exportacionReporteRepository.save(exportacionReporte);
    }

    public List<ExportacionReporte> obtenerExportacionesPorReporte(Long idReporte) {
        return exportacionReporteRepository.findByReporteIdReporte(idReporte);
    }
}
