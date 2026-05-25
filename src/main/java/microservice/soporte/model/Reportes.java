package microservice.soporte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reportes")
public class Reportes {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idReporte;

@Column(nullable = false)
@NotBlank(message = "La fecha es obligatoria")
private String fechaReporte;

@Column(nullable = false)
@NotBlank(message = "La razon es obligatoria")
private String razonReporte;

@Column(nullable = false)
@NotBlank(message = "La descripcion es obligatoria")
private String descripcionReporte;

@Column(nullable = false)
@NotBlank(message = "El estado es obligatorio")
private String estadoReporte;
}
