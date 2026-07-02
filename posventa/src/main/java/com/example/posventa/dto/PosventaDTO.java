package com.example.posventa.dto;

import java.time.LocalDateTime;

import com.example.posventa.model.Posventa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PosventaDTO {
 
    private Long idPosventa;
 
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;
 
    @NotNull(message = "El ID de la compra es obligatorio")
    private Long idCompra;
 
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres")
    private String motivo;
 
    private String estado;
 
    private LocalDateTime fecha;
 
    public Posventa toModel() {
        return Posventa.builder()
                .idPosventa(this.idPosventa)
                .idCliente(this.idCliente)
                .idCompra(this.idCompra)
                .motivo(this.motivo != null ? this.motivo.trim() : null)
                .estado(this.estado != null ? this.estado.trim().toUpperCase() : null)
                .fecha(this.fecha)
                .build();
    }
 
    public static PosventaDTO fromModel(Posventa p) {
        if (p == null) return null;
        return PosventaDTO.builder()
                .idPosventa(p.getIdPosventa())
                .idCliente(p.getIdCliente())
                .idCompra(p.getIdCompra())
                .motivo(p.getMotivo())
                .estado(p.getEstado())
                .fecha(p.getFecha())
                .build();
    }
}