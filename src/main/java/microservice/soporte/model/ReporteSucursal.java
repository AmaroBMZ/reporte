package microservice.soporte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reportes_sucursal")
public class ReporteSucursal extends Reportes {
    private int idSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombreSucursal;

    @PositiveOrZero(message = "Las ventas de la sucursal no pueden ser negativas")
    private double ventasSucursal;

    private double rendimiento;

    public void generarReporteSucursal() {
        setTipo("Sucursal");
        generar();
    }

    public double compararMetricas(double valorReferencia) {
        return rendimiento - valorReferencia;
    }
}
