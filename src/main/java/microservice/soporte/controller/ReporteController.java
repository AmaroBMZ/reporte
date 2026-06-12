package microservice.soporte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.soporte.model.ExportacionReporte;
import microservice.soporte.model.Metrica;
import microservice.soporte.model.ReporteInventario;
import microservice.soporte.model.ReporteSucursal;
import microservice.soporte.model.ReporteVentas;
import microservice.soporte.model.Reportes;
import microservice.soporte.service.ReporteService;

@RestController
@RequestMapping("api/v1/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;


    @PostMapping
    public Reportes postReporte(@Valid @RequestBody Reportes reportes) {
        return reporteService.crearReportes(reportes);
    }

    @GetMapping
    public List<Reportes> getReportes() {
        return reporteService.obtenerReportes();
    }

    @PutMapping("{id}")
    public Reportes putReportes(@PathVariable Long id, @Valid @RequestBody Reportes reportes) {
        return reporteService.updateReportes(id, reportes);
    }

    @DeleteMapping("{id}")
    public void eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
    }

    @PostMapping("ventas")
    public ReporteVentas postReporteVentas(@Valid @RequestBody ReporteVentas reporteVentas) {
        return reporteService.generarReporteVentas(reporteVentas);
    }

    @GetMapping("ventas")
    public List<ReporteVentas> getReportesVentas() {
        return reporteService.obtenerReportesVentas();
    }

    @PostMapping("inventario")
    public ReporteInventario postReporteInventario(@Valid @RequestBody ReporteInventario reporteInventario) {
        return reporteService.generarReporteInventario(reporteInventario);
    }

    @GetMapping("inventario")
    public List<ReporteInventario> getReportesInventario() {
        return reporteService.obtenerReportesInventario();
    }

    @PostMapping("sucursal")
    public ReporteSucursal postReporteSucursal(@Valid @RequestBody ReporteSucursal reporteSucursal) {
        return reporteService.generarReporteSucursal(reporteSucursal);
    }

    @GetMapping("sucursal")
    public List<ReporteSucursal> getReportesSucursal() {
        return reporteService.obtenerReportesSucursal();
    }

    @PostMapping("{id}/metricas")
    public Metrica postMetrica(@PathVariable Long id, @Valid @RequestBody Metrica metrica) {
        return reporteService.agregarMetrica(id, metrica);
    }

    @GetMapping("{id}/metricas")
    public List<Metrica> getMetricas(@PathVariable Long id) {
        return reporteService.obtenerMetricasPorReporte(id);
    }

    @PostMapping("{id}/exportaciones")
    public ExportacionReporte postExportacion(
            @PathVariable Long id,
            @Valid @RequestBody ExportacionReporte exportacionReporte) {
        return reporteService.exportarReporte(id, exportacionReporte);
    }

    @GetMapping("{id}/exportaciones")
    public List<ExportacionReporte> getExportaciones(@PathVariable Long id) {
        return reporteService.obtenerExportacionesPorReporte(id);
    }
}
