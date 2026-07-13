package com.bravatta.fidelizacion.dto;

import com.bravatta.fidelizacion.model.Fidelizacion;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FidelizacionDTO {

    private Long idFidelizacion;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    private Integer puntosAcumulados;

    private String nivel;

    private Boolean cuponCumpleanos;

    private LocalDate fechaNacimiento;

    public Fidelizacion toModel() {
        return Fidelizacion.builder()
                .idFidelizacion(this.idFidelizacion)
                .idCliente(this.idCliente)
                .puntosAcumulados(this.puntosAcumulados != null ? this.puntosAcumulados : 0)
                .nivel(this.nivel != null ? this.nivel.trim().toUpperCase() : "PRINCIPIANTE")
                .cuponCumpleanos(this.cuponCumpleanos != null ? this.cuponCumpleanos : false)
                .fechaNacimiento(this.fechaNacimiento)
                .build();
    }

    public static FidelizacionDTO fromModel(Fidelizacion f) {
        if (f == null) return null;
        return FidelizacionDTO.builder()
                .idFidelizacion(f.getIdFidelizacion())
                .idCliente(f.getIdCliente())
                .puntosAcumulados(f.getPuntosAcumulados())
                .nivel(f.getNivel())
                .cuponCumpleanos(f.getCuponCumpleanos())
                .fechaNacimiento(f.getFechaNacimiento())
                .build();
    }
}