package com.bravatta.clientes.dto;

import com.bravatta.clientes.model.Cliente;
import com.bravatta.clientes.model.Direccion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private Long id_cliente;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9kK]{1})|(\\d{7,8}-[0-9kK]{1})$", message = "RUT no válido")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @Valid
    @NotNull(message = "Los datos de la direccion son obligatorios")
    private DireccionDTO direccion;
    
    public Cliente toModel() {
        Cliente c = Cliente.builder()
                .idCliente(this.id_cliente)
                .rut(this.rut != null ? this.rut.trim().toUpperCase() : null)
                .correo(this.correo != null ? this.correo.trim().toLowerCase() : null)
                .nombre(this.nombre)
                .build();
        if (this.direccion != null) {
            Direccion d = Direccion.builder()
                .id(this.direccion.getId())
                .calle(this.direccion.getCalle())
                .comuna(this.direccion.getComuna())
                .cliente(c)
                .build();
        c.setDirecciones(List.of(d));}
        return c;
    }

    public static ClienteDTO fromModel(Cliente c, Direccion d) {
        if (c == null) return null;
        return ClienteDTO.builder()
                .id_cliente(c.getIdCliente())
                .rut(c.getRut())
                .correo(c.getCorreo())
                .nombre(c.getNombre())
                .direccion(DireccionDTO.fromModel(d))
                .build();
    }


}

