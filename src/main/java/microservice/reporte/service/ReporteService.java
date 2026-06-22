package microservice.reporte.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.reporte.model.ExportacionReporte;
import microservice.reporte.model.DetalleInventario;
import microservice.reporte.model.DetalleSucursal;
import microservice.reporte.model.DetalleVentas;
import microservice.reporte.model.Metrica;
import microservice.reporte.model.ReporteInventario;
import microservice.reporte.model.ReporteSucursal;
import microservice.reporte.model.ReporteVentas;
import microservice.reporte.model.Reportes;
import microservice.reporte.repository.ExportacionReporteRepository;
import microservice.reporte.repository.DetalleInventarioRepository;
import microservice.reporte.repository.DetalleSucursalRepository;
import microservice.reporte.repository.DetalleVentasRepository;
import microservice.reporte.repository.MetricaRepository;
import microservice.reporte.repository.ReporteInventarioRepository;
import microservice.reporte.repository.ReporteRepository;
import microservice.reporte.repository.ReporteSucursalRepository;
import microservice.reporte.repository.ReporteVentasRepository;

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

    @Autowired
    private DetalleVentasRepository detalleVentasRepository;

    @Autowired
    private DetalleInventarioRepository detalleInventarioRepository;

    @Autowired
    private DetalleSucursalRepository detalleSucursalRepository;

    public Reportes crearReportes(Reportes reporte){
        reporte.completarDatosDelDiagrama();
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
            reporteExistente.setFechaGeneracion(reportes.getFechaGeneracion());
            reporteExistente.setPeriodoInicio(reportes.getPeriodoInicio());
            reporteExistente.setPeriodoFin(reportes.getPeriodoFin());
            reporteExistente.setEstado(reportes.getEstado());
            return reporteRepository.save(reporteExistente);
        }
        return null;
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public ReporteVentas generarReporteVentas(ReporteVentas reporteVentas) {
        vincularDetallesVentas(reporteVentas);
        reporteVentas.generarReporteVentas();
        return reporteVentasRepository.save(reporteVentas);
    }

    public List<ReporteVentas> obtenerReportesVentas() {
        return reporteVentasRepository.findAll();
    }

    public ReporteInventario generarReporteInventario(ReporteInventario reporteInventario) {
        vincularDetallesInventario(reporteInventario);
        reporteInventario.generarReporteInventario();
        return reporteInventarioRepository.save(reporteInventario);
    }

    public List<ReporteInventario> obtenerReportesInventario() {
        return reporteInventarioRepository.findAll();
    }

    public ReporteSucursal generarReporteSucursal(ReporteSucursal reporteSucursal) {
        vincularDetallesSucursal(reporteSucursal);
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

    public Metrica actualizarMetrica(Long idMetrica, Metrica datos) {
        Metrica metrica = metricaRepository.findById(idMetrica).orElse(null);
        if (metrica == null) {
            return null;
        }
        metrica.setNombre(datos.getNombre());
        metrica.actualizarMetrica(datos.getValor(), datos.getUnidad());
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

    public DetalleVentas agregarDetalleVentas(Long idReporte, DetalleVentas detalle) {
        ReporteVentas reporte = reporteVentasRepository.findById(idReporte).orElse(null);
        if (reporte == null) {
            return null;
        }
        detalle.setReporteVentas(reporte);
        detalle.agregarDetalle();
        return detalleVentasRepository.save(detalle);
    }

    public List<DetalleVentas> obtenerDetallesVentas(Long idReporte) {
        return detalleVentasRepository.findByIdReporte(idReporte);
    }

    public DetalleInventario agregarDetalleInventario(Long idReporte, DetalleInventario detalle) {
        ReporteInventario reporte = reporteInventarioRepository.findById(idReporte).orElse(null);
        if (reporte == null) {
            return null;
        }
        detalle.setReporteInventario(reporte);
        detalle.agregarDetalle();
        return detalleInventarioRepository.save(detalle);
    }

    public List<DetalleInventario> obtenerDetallesInventario(Long idReporte) {
        return detalleInventarioRepository.findByIdReporte(idReporte);
    }

    public DetalleSucursal agregarDetalleSucursal(Long idReporte, DetalleSucursal detalle) {
        ReporteSucursal reporte = reporteSucursalRepository.findById(idReporte).orElse(null);
        if (reporte == null) {
            return null;
        }
        detalle.setReporteSucursal(reporte);
        detalle.agregarDetalle();
        return detalleSucursalRepository.save(detalle);
    }

    public List<DetalleSucursal> obtenerDetallesSucursal(Long idReporte) {
        return detalleSucursalRepository.findByIdReporte(idReporte);
    }

    private void vincularDetallesVentas(ReporteVentas reporteVentas) {
        if (reporteVentas.getDetalles() != null) {
            reporteVentas.getDetalles().forEach(detalle -> detalle.setReporteVentas(reporteVentas));
        }
    }

    private void vincularDetallesInventario(ReporteInventario reporteInventario) {
        if (reporteInventario.getDetalles() != null) {
            reporteInventario.getDetalles().forEach(detalle -> detalle.setReporteInventario(reporteInventario));
        }
    }

    private void vincularDetallesSucursal(ReporteSucursal reporteSucursal) {
        if (reporteSucursal.getDetalles() != null) {
            reporteSucursal.getDetalles().forEach(detalle -> detalle.setReporteSucursal(reporteSucursal));
        }
    }
}
