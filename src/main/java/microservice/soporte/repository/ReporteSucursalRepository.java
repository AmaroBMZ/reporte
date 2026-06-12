package microservice.soporte.repository;

import microservice.soporte.model.ReporteSucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteSucursalRepository extends JpaRepository<ReporteSucursal, Long> {
}
