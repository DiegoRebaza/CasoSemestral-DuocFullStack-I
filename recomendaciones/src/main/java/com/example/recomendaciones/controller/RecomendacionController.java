package com.example.recomendaciones.controller;

import com.example.recomendaciones.dto.RecomendacionDTO;
import com.example.recomendaciones.service.RecomendacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRecomendacion(@PathVariable Long id) {
        recomendacionService.eliminar(id);
        return ResponseEntity.ok("Recomendación eliminada exitosamente.");
    }
}