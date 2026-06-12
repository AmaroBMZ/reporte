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
@Table(name = "reportes_ventas")
public class ReporteVentas extends Reportes {
    @PositiveOrZero(message = "El total de ventas no puede ser negativo")
    private double totalVentas;

    @PositiveOrZero(message = "La cantidad de ventas no puede ser negativa")
    private int cantidadVentas;

    @Column(nullable = false)
    @NotBlank(message = "El periodo es obligatorio")
    private String periodo;

    private int idSucursal;

    public void generarReporteVentas() {
        setTipo("Ventas");
        generar();
    }

    public double calcularTotales() {
        return totalVentas;
    }
}
