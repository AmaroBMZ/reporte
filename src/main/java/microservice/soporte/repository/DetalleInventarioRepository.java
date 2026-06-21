package microservice.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.soporte.model.DetalleInventario;

@Repository
public interface DetalleInventarioRepository extends JpaRepository<DetalleInventario, Long> {
    List<DetalleInventario> findByIdReporte(Long idReporte);
}
