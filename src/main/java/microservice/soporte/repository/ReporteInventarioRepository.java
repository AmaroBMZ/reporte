package microservice.soporte.repository;

import microservice.soporte.model.ReporteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteInventarioRepository extends JpaRepository<ReporteInventario, Long> {
}
