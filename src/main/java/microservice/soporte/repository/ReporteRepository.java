package microservice.soporte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.soporte.model.Reportes;

@Repository
public interface ReporteRepository extends JpaRepository<Reportes, Long> {
    
}
