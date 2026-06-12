package microservice.soporte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reportes_inventario")
public class ReporteInventario extends Reportes {
    @PositiveOrZero(message = "El total de productos no puede ser negativo")
    private int totalProductos;

    @PositiveOrZero(message = "El stock bajo no puede ser negativo")
    private int stockBajo;

    @PositiveOrZero(message = "Los movimientos no pueden ser negativos")
    private int movimientos;

    private LocalDate fechaCorte;

    public void generarReporteInventario() {
        setTipo("Inventario");
        generar();
    }

    public int calcularStock() {
        return totalProductos - stockBajo;
    }
}
