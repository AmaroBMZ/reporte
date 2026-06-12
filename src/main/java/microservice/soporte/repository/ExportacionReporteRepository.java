package microservice.soporte.repository;

import java.util.List;
import microservice.soporte.model.ExportacionReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportacionReporteRepository extends JpaRepository<ExportacionReporte, Long> {
    List<ExportacionReporte> findByReporteIdReporte(Long idReporte);
}
