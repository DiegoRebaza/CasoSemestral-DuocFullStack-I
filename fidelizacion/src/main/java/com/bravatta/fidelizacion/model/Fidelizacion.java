package com.bravatta.fidelizacion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fidelizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fidelizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fidelizacion")
    private Long idFidelizacion;

    @Column(name = "id_cliente", nullable = false, unique = true)
    private Long idCliente;

    @Column(name = "puntos_acumulados", nullable = false)
    private Integer puntosAcumulados;

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel;

    @Column(name = "cupon_cumpleanos", nullable = false)
    private Boolean cuponCumpleanos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @PrePersist
    protected void onCreate() {
        if (this.puntosAcumulados == null) this.puntosAcumulados = 0;
        if (this.nivel == null) this.nivel = "PRINCIPIANTE";
        if (this.cuponCumpleanos == null) this.cuponCumpleanos = false;
    }
}