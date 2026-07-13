package com.bravatta.notificacion.assembler;

import com.bravatta.notificacion.controller.NotificacionController;
import com.bravatta.notificacion.controller.NotificacionControllerV2; 
import com.bravatta.notificacion.dto.NotificacionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<NotificacionDTO, EntityModel<NotificacionDTO>> {

    @Override
    public EntityModel<NotificacionDTO> toModel(NotificacionDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(NotificacionControllerV2.class).obtenerNotificacion(dto.getIdNotificacion())).withSelfRel(),
                linkTo(methodOn(NotificacionControllerV2.class).listarNotificaciones()).withRel("notificaciones"),
                linkTo(methodOn(NotificacionController.class).buscarPorCliente(dto.getIdCliente())).withRel("por-cliente"));
    }
}