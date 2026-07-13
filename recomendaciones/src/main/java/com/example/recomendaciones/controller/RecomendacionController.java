package com.example.recomendaciones.controller;

import com.example.recomendaciones.dto.RecomendacionDTO;
import com.example.recomendaciones.model.Recomendacion;
import com.example.recomendaciones.service.RecomendacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    @PostMapping
    public ResponseEntity<RecomendacionDTO> crearRecomendacion(@Valid @RequestBody RecomendacionDTO dto) {
        RecomendacionDTO resultado = recomendacionService.guardar(dto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecomendacionDTO>> listarRecomendaciones() {
        List<RecomendacionDTO> resultado = recomendacionService.listar().stream()
                .map(RecomendacionDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionDTO> obtenerPorId(@PathVariable Long id) {
        Recomendacion recomendacion = recomendacionService.obtenerPorId(id);
        return ResponseEntity.ok(RecomendacionDTO.fromModel(recomendacion));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeRecomendacion(@PathVariable Long id) {
        return ResponseEntity.ok(recomendacionService.existePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecomendacionDTO> actualizarRecomendacion(
            @PathVariable Long id, @Valid @RequestBody RecomendacionDTO dto) {
        RecomendacionDTO actualizado = recomendacionService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRecomendacion(@PathVariable Long id) {
        recomendacionService.eliminar(id);
        return ResponseEntity.ok("Recomendación eliminada exitosamente.");
    }
}