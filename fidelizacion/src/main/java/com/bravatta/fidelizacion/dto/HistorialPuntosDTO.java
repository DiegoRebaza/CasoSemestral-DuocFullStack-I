package com.bravatta.fidelizacion.dto;

import com.bravatta.fidelizacion.model.HistorialPuntos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialPuntosDTO {

    private Long idHistorial;

    @NotNull(message = "El ID de fidelización es obligatorio")
    private Long idFidelizacion;

    @NotNull(message = "El ID del pago es obligatorio")
    private Long idPago;

    @NotNull(message = "Los puntos sumados son obligatorios")
    @Min(value = 1, message = "Los puntos sumados deben ser al menos 1")
    private Integer puntosSumados;

    private LocalDateTime fecha;

    private String descripcion;

    public HistorialPuntos toModel() {
        return HistorialPuntos.builder()
                .idHistorial(this.idHistorial)
                .idFidelizacion(this.idFidelizacion)
                .idPago(this.idPago)
                .puntosSumados(this.puntosSumados)
                .fecha(this.fecha)
                .descripcion(this.descripcion)
                .build();
    }

    public static HistorialPuntosDTO fromModel(HistorialPuntos h) {
        if (h == null) return null;
        return HistorialPuntosDTO.builder()
                .idHistorial(h.getIdHistorial())
                .idFidelizacion(h.getIdFidelizacion())
                .idPago(h.getIdPago())
                .puntosSumados(h.getPuntosSumados())
                .fecha(h.getFecha())
                .descripcion(h.getDescripcion())
                .build();
    }
}