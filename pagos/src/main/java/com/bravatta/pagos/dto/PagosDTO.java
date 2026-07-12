package com.bravatta.pagos.dto;
import com.bravatta.pagos.model.Pagos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagosDTO {

    private Long idTransaccion;

    @NotNull(message = "Debes ingresar un monto")
    @Positive(message = "Debe ser un valor positivo mayor a cero")
    private Integer monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 30, message = "El metodo de pago no puede superar los 30 caracteres")
    private String metodoPago;
    @NotBlank(message = "El codigo es obligatorio")
    private String idTransaccionExterna;
    
    public Pagos toModel() {
        return Pagos.builder()
                .idTransaccion(this.idTransaccion)
                .monto(this.monto)
                .metodoPago(this.metodoPago != null ? this.metodoPago.trim().toUpperCase() : null) // Ej: "WEBPAY"
                .idTransaccionExterna(this.idTransaccionExterna != null ? this.idTransaccionExterna.trim() : null)
                .build();
    }

    public static PagosDTO fromModel(Pagos pago) {
        if (pago == null) return null;

        return PagosDTO.builder()
                .idTransaccion(pago.getIdTransaccion())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .idTransaccionExterna(pago.getIdTransaccionExterna())
                .build();
    }

}

