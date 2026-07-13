package com.bravatta.envios.controller;

import com.bravatta.envios.dto.EnviosDTO;
import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import com.bravatta.envios.service.EnviosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnviosController {

    private final EnviosService enviosService;

    @GetMapping
    public ResponseEntity<List<EnviosDTO>> listarTodos() {
        List<EnviosDTO> resultado = enviosService.listarTodos().stream()
                .map(EnviosDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnviosDTO> buscarPorId(@PathVariable Long id) {
        Envios envio = enviosService.buscarPorId(id);
        return ResponseEntity.ok(EnviosDTO.fromModel(envio));
    }

    @GetMapping("/compra/{idCompra}")
    public ResponseEntity<List<EnviosDTO>> buscarPorCompra(@PathVariable Long idCompra) {
        List<EnviosDTO> resultado = enviosService.buscarPorCompra(idCompra).stream()
                .map(EnviosDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EnviosDTO>> buscarPorEstado(@PathVariable EstadoEnvio estado) {
        List<EnviosDTO> resultado = enviosService.buscarPorEstado(estado).stream()
                .map(EnviosDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/repartidor/{repartidor}")
    public ResponseEntity<List<EnviosDTO>> buscarPorRepartidor(@PathVariable String repartidor) {
        List<EnviosDTO> resultado = enviosService.buscarPorRepartidor(repartidor).stream()
                .map(EnviosDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<EnviosDTO> crear(@Valid @RequestBody EnviosDTO dto) {
        Envios creado = enviosService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnviosDTO.fromModel(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnviosDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EnviosDTO dto) {
        Envios actualizado = enviosService.actualizar(id, dto);
        return ResponseEntity.ok(EnviosDTO.fromModel(actualizado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnviosDTO> actualizarEstado(@PathVariable Long id, @RequestParam EstadoEnvio nuevoEstado) {
        Envios actualizado = enviosService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(EnviosDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        enviosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}