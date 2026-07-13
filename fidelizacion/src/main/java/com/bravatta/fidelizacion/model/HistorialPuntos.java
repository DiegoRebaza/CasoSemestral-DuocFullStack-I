package com.bravatta.fidelizacion.model;
 
import jakarta.persistence.*;
import lombok.*;
 
import java.time.LocalDateTime;
 
@Entity
@Table(name = "historial_puntos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialPuntos {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;
 
    @Column(name = "id_fidelizacion", nullable = false)
    private Long idFidelizacion;
 
    @Column(name = "id_pago", nullable = false)
    private Long idPago;
 
    @Column(name = "puntos_sumados", nullable = false)
    private Integer puntosSumados;
 
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
 
    @Column(name = "descripcion", length = 255)
    private String descripcion;
 
    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}
 
