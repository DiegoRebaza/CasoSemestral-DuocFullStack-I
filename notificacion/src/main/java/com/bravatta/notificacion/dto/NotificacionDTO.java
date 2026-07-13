package com.bravatta.notificacion.dto;

import com.bravatta.notificacion.model.Notificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDTO {

    private Long idNotificacion;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El evento es obligatorio")
    private String evento;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String mensaje;

    private String estado;

    private LocalDateTime fechaEnvio;

    public Notificacion toModel() {
        return Notificacion.builder()
                .idNotificacion(this.idNotificacion)
                .idCliente(this.idCliente)
                .tipo(this.tipo != null ? this.tipo.trim().toUpperCase() : "SMS")
                .evento(this.evento != null ? this.evento.trim().toUpperCase() : null)
                .mensaje(this.mensaje != null ? this.mensaje.trim() : null)
                .estado("ENVIADO")
                .fechaEnvio(this.fechaEnvio)
                .build();
    }

    public static NotificacionDTO fromModel(Notificacion n) {
        if (n == null) return null;
        return NotificacionDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .idCliente(n.getIdCliente())
                .tipo(n.getTipo())
                .evento(n.getEvento())
                .mensaje(n.getMensaje())
                .estado(n.getEstado())
                .fechaEnvio(n.getFechaEnvio())
                .build();
    }
}