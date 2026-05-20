package microservice.soporte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.soporte.model.Reportes;
import microservice.soporte.repository.ReporteRepository;

@Service
@Transactional
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

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
        }
        return reporteRepository.save(reporteExistente);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}
