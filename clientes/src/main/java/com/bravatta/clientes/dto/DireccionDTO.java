package com.bravatta.clientes.dto;

import com.bravatta.clientes.model.Cliente;
import com.bravatta.clientes.model.Direccion;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    private Long id;

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    public Direccion toModel(Cliente cliente) {
        return Direccion.builder()
                .id(this.id)
                .calle(this.calle)
                .comuna(this.comuna)
                .cliente(cliente)
                .build();
    }

    public static DireccionDTO fromModel(Direccion d) {
        if (d == null) return null;
        return new DireccionDTO(d.getId(), d.getCalle(), d.getComuna());
    }
}