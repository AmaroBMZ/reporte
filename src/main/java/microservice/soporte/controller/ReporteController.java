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
}
