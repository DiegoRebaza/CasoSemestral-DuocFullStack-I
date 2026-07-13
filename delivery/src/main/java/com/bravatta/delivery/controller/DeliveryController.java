package com.bravatta.delivery.controller;

import com.bravatta.delivery.dto.DeliveryDTO;
import com.bravatta.delivery.model.Delivery;
import com.bravatta.delivery.model.EstadoEnvio;
import com.bravatta.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> listarTodos() {
        List<DeliveryDTO> resultado = deliveryService.listarTodos().stream()
                .map(DeliveryDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> buscarPorId(@PathVariable Long id) {
        Delivery delivery = deliveryService.buscarPorId(id);
        return ResponseEntity.ok(DeliveryDTO.fromModel(delivery));
    }

    @GetMapping("/compra/{idCompra}")
    public ResponseEntity<List<DeliveryDTO>> buscarPorCompra(@PathVariable Long idCompra) {
        List<DeliveryDTO> resultado = deliveryService.buscarPorCompra(idCompra).stream()
                .map(DeliveryDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DeliveryDTO>> buscarPorEstado(@PathVariable EstadoEnvio estado) {
        List<DeliveryDTO> resultado = deliveryService.buscarPorEstado(estado).stream()
                .map(DeliveryDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/repartidor/{repartidor}")
    public ResponseEntity<List<DeliveryDTO>> buscarPorRepartidor(@PathVariable String repartidor) {
        List<DeliveryDTO> resultado = deliveryService.buscarPorRepartidor(repartidor).stream()
                .map(DeliveryDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> crear(@Valid @RequestBody DeliveryDTO dto) {
        Delivery creado = deliveryService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeliveryDTO.fromModel(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DeliveryDTO dto) {
        Delivery actualizado = deliveryService.actualizar(id, dto);
        return ResponseEntity.ok(DeliveryDTO.fromModel(actualizado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<DeliveryDTO> actualizarEstado(@PathVariable Long id, @RequestParam EstadoEnvio nuevoEstado) {
        Delivery actualizado = deliveryService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(DeliveryDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        deliveryService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}