package com.bravatta.fidelizacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.bravatta.fidelizacion.assembler.FidelizacionModelAssembler;
import com.bravatta.fidelizacion.dto.FidelizacionDTO;
import com.bravatta.fidelizacion.dto.HistorialPuntosDTO;
import com.bravatta.fidelizacion.exception.BadRequestException;
import com.bravatta.fidelizacion.exception.ResourceNotFoundException;
import com.bravatta.fidelizacion.service.FidelizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)

@DisplayName("FidelizacionController — PruebaUnitaria")
class FidelizacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FidelizacionService fidelizacionService;

    @Mock
    private FidelizacionModelAssembler assembler;

    @InjectMocks
    private FidelizacionController fidelizacionController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private FidelizacionDTO response1;
    private FidelizacionDTO response2;
    private FidelizacionDTO response3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fidelizacionController).build();

        response1 = FidelizacionDTO.builder()
                .idFidelizacion(1L).idCliente(1L)
                .puntosAcumulados(50).nivel("PRINCIPIANTE")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1995, 7, 12))
                .build();

        response2 = FidelizacionDTO.builder()
                .idFidelizacion(2L).idCliente(2L)
                .puntosAcumulados(120).nivel("AMANTE_DEL_HELADO")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1990, 3, 22))
                .build();

        response3 = FidelizacionDTO.builder()
                .idFidelizacion(3L).idCliente(3L)
                .puntosAcumulados(310).nivel("MAESTRO_HELADERO")
                .cuponCumpleanos(true)
                .fechaNacimiento(LocalDate.of(1988, 11, 5))
                .build();
    }

    // POST /api/fidelizacion
    @Nested
    @DisplayName("POST /api/fidelizacion")
    class CrearFidelizacion {

        @Test
        @DisplayName("Debería crear una ficha y retornar 201 CREATED")
        void deberiaCrearFicha() throws Exception {
            // GIVEN
            FidelizacionDTO dtoEntrada = FidelizacionDTO.builder()
                    .idCliente(1L).puntosAcumulados(0).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false).fechaNacimiento(LocalDate.of(1995, 7, 12))
                    .build();

            when(fidelizacionService.crear(any(FidelizacionDTO.class))).thenReturn(response1);

            // WHEN / THEN
            mockMvc.perform(post("/api/fidelizacion")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dtoEntrada)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idFidelizacion", is(1)))
                    .andExpect(jsonPath("$.nivel", is("PRINCIPIANTE")))
                    .andExpect(jsonPath("$.puntosAcumulados", is(50)));

            verify(fidelizacionService, times(1)).crear(any(FidelizacionDTO.class));
        }

        @Test
        @DisplayName("Debería retornar 400 si falta idCliente")
        void deberiaFallarSinIdCliente() throws Exception {
            // GIVEN — idCliente es @NotNull
            FidelizacionDTO dtoInvalido = FidelizacionDTO.builder()
                    .idCliente(null).nivel("PRINCIPIANTE").build();

            // WHEN / THEN
            mockMvc.perform(post("/api/fidelizacion")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dtoInvalido)))
                    .andExpect(status().isBadRequest());

            verify(fidelizacionService, never()).crear(any());
        }
    }

    // GET /api/fidelizacion
    @Nested
    @DisplayName("GET /api/fidelizacion")
    class ListarFidelizaciones {

        @Test
        @DisplayName("Debería listar todas las fichas y retornar 200 OK")
        void deberiaListarTodas() throws Exception {
            // GIVEN
            when(fidelizacionService.listar()).thenReturn(List.of(response1, response2, response3));
            when(assembler.toModel(any(FidelizacionDTO.class)))
                    .thenAnswer(inv -> EntityModel.of(inv.getArgument(0)));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion"))
                    .andExpect(status().isOk());

            verify(fidelizacionService, times(1)).listar();
        }
    }

    // GET /api/fidelizacion/{id}
    @Nested
    @DisplayName("GET /api/fidelizacion/{id}")
    class ObtenerFidelizacion {

        @Test
        @DisplayName("Debería retornar ficha existente con 200 OK")
        void deberiaRetornarFicha() throws Exception {
            // GIVEN
            when(fidelizacionService.obtenerPorId(1L)).thenReturn(response1);
            when(assembler.toModel(response1)).thenReturn(EntityModel.of(response1));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idFidelizacion", is(1)))
                    .andExpect(jsonPath("$.idCliente", is(1)))
                    .andExpect(jsonPath("$.nivel", is("PRINCIPIANTE")));

            verify(fidelizacionService, times(1)).obtenerPorId(1L);
        }

        @Test
        @DisplayName("Debería retornar 404 si la ficha no existe")
        void deberiaRetornar404() throws Exception {
            // GIVEN
            when(fidelizacionService.obtenerPorId(99L))
                    .thenThrow(new ResourceNotFoundException("Ficha de fidelización no encontrada con ID: 99"));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/99"))
                    .andExpect(status().isNotFound());
        }
    }

    // GET /api/fidelizacion/{id}/exists
    @Nested
    @DisplayName("GET /api/fidelizacion/{id}/exists")
    class ExisteFidelizacion {

        @Test
        @DisplayName("Debería retornar true si la ficha existe")
        void deberiaRetornarTrue() throws Exception {
            when(fidelizacionService.existePorId(1L)).thenReturn(true);

            mockMvc.perform(get("/api/fidelizacion/1/exists"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        @DisplayName("Debería retornar false si la ficha no existe")
        void deberiaRetornarFalse() throws Exception {
            when(fidelizacionService.existePorId(99L)).thenReturn(false);

            mockMvc.perform(get("/api/fidelizacion/99/exists"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
    }

    // GET /api/fidelizacion/cliente/{idCliente}
    @Nested
    @DisplayName("GET /api/fidelizacion/cliente/{idCliente}")
    class ObtenerPorCliente {

        @Test
        @DisplayName("Debería retornar ficha del cliente con 200 OK")
        void deberiaRetornarFichaDeCliente() throws Exception {
            // GIVEN
            when(fidelizacionService.obtenerPorCliente(2L)).thenReturn(response2);
            when(assembler.toModel(response2)).thenReturn(EntityModel.of(response2));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/cliente/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idCliente", is(2)))
                    .andExpect(jsonPath("$.nivel", is("AMANTE_DEL_HELADO")));
        }

        @Test
        @DisplayName("Debería retornar 404 si el cliente no tiene ficha")
        void deberiaRetornar404SiSinFicha() throws Exception {
            // GIVEN
            when(fidelizacionService.obtenerPorCliente(99L))
                    .thenThrow(new ResourceNotFoundException("No hay ficha de fidelización para el cliente ID: 99"));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/cliente/99"))
                    .andExpect(status().isNotFound());
        }
    }

    // PUT /api/fidelizacion/sumar-puntos/{idCliente}
    @Nested
    @DisplayName("PUT /api/fidelizacion/sumar-puntos/{idCliente}")
    class SumarPuntos {

        @Test
        @DisplayName("Debería sumar puntos y retornar 200 OK con ficha actualizada")
        void deberiaSumarPuntos() throws Exception {
            // GIVEN
            FidelizacionDTO actualizada = FidelizacionDTO.builder()
                    .idFidelizacion(1L).idCliente(1L)
                    .puntosAcumulados(60).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false).build();

            when(fidelizacionService.sumarPuntosPorPago(1L, 5L)).thenReturn(actualizada);

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/sumar-puntos/1")
                            .param("idPago", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.puntosAcumulados", is(60)))
                    .andExpect(jsonPath("$.nivel", is("PRINCIPIANTE")));

            verify(fidelizacionService, times(1)).sumarPuntosPorPago(1L, 5L);
        }

        @Test
        @DisplayName("Debería retornar 400 si el pago ya fue procesado")
        void deberiaRetornar400SiPagoDuplicado() throws Exception {
            // GIVEN
            when(fidelizacionService.sumarPuntosPorPago(1L, 1L))
                    .thenThrow(new BadRequestException("El pago ID 1 ya fue registrado en esta ficha de fidelización"));

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/sumar-puntos/1")
                            .param("idPago", "1"))
                    .andExpect(status().isBadRequest());
        }
    }

    // PUT /api/fidelizacion/cupon-cumpleanos/{idCliente}/activar
    @Nested
    @DisplayName("PUT /api/fidelizacion/cupon-cumpleanos/{idCliente}/activar")
    class ActivarCupon {

        @Test
        @DisplayName("Debería activar cupón y retornar 200 OK")
        void deberiaActivarCupon() throws Exception {
            // GIVEN
            FidelizacionDTO conCupon = FidelizacionDTO.builder()
                    .idFidelizacion(3L).idCliente(3L)
                    .puntosAcumulados(310).nivel("MAESTRO_HELADERO")
                    .cuponCumpleanos(true).build();

            when(fidelizacionService.activarCuponCumpleanos(3L)).thenReturn(conCupon);

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/cupon-cumpleanos/3/activar"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cuponCumpleanos", is(true)));

            verify(fidelizacionService, times(1)).activarCuponCumpleanos(3L);
        }

        @Test
        @DisplayName("Debería retornar 400 si hoy no es el cumpleaños")
        void deberiaRetornar400SiNoEsCumpleanos() throws Exception {
            // GIVEN
            when(fidelizacionService.activarCuponCumpleanos(1L))
                    .thenThrow(new BadRequestException("Hoy no es el cumpleaños del cliente."));

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/cupon-cumpleanos/1/activar"))
                    .andExpect(status().isBadRequest());
        }
    }

    // PUT /api/fidelizacion/cupon-cumpleanos/{idCliente}/canjear
    @Nested
    @DisplayName("PUT /api/fidelizacion/cupon-cumpleanos/{idCliente}/canjear")
    class CanjearCupon {

        @Test
        @DisplayName("Debería canjear cupón y retornar 200 OK")
        void deberiaCanjearCupon() throws Exception {
            // GIVEN
            FidelizacionDTO canjeado = FidelizacionDTO.builder()
                    .idFidelizacion(3L).idCliente(3L)
                    .puntosAcumulados(310).nivel("MAESTRO_HELADERO")
                    .cuponCumpleanos(false).build();

            when(fidelizacionService.canjearCuponCumpleanos(3L)).thenReturn(canjeado);

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/cupon-cumpleanos/3/canjear"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cuponCumpleanos", is(false)));

            verify(fidelizacionService, times(1)).canjearCuponCumpleanos(3L);
        }

        @Test
        @DisplayName("Debería retornar 400 si el cliente no tiene cupón activo")
        void deberiaRetornar400SiSinCupon() throws Exception {
            // GIVEN
            when(fidelizacionService.canjearCuponCumpleanos(1L))
                    .thenThrow(new BadRequestException("El cliente no tiene un cupón de cumpleaños activo"));

            // WHEN / THEN
            mockMvc.perform(put("/api/fidelizacion/cupon-cumpleanos/1/canjear"))
                    .andExpect(status().isBadRequest());
        }
    }

    // GET /api/fidelizacion/cliente/{idCliente}/historial
    @Nested
    @DisplayName("GET /api/fidelizacion/cliente/{idCliente}/historial")
    class ObtenerHistorial {

        @Test
        @DisplayName("Debería retornar historial de puntos del cliente")
        void deberiaRetornarHistorial() throws Exception {
            // GIVEN
            HistorialPuntosDTO h = HistorialPuntosDTO.builder()
                    .idHistorial(1L).idFidelizacion(1L).idPago(1L)
                    .puntosSumados(10).descripcion("Puntos por pago ID: 1").build();

            when(fidelizacionService.obtenerHistorial(1L)).thenReturn(List.of(h));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/cliente/1/historial"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].puntosSumados", is(10)))
                    .andExpect(jsonPath("$[0].descripcion", is("Puntos por pago ID: 1")));

            verify(fidelizacionService, times(1)).obtenerHistorial(1L);
        }
    }

    // GET /api/fidelizacion/buscar/nivel
    @Nested
    @DisplayName("GET /api/fidelizacion/buscar/nivel")
    class BuscarPorNivel {

        @Test
        @DisplayName("Debería retornar clientes MAESTRO_HELADERO con 200 OK")
        void deberiaRetornarMaestros() throws Exception {
            // GIVEN
            when(fidelizacionService.buscarPorNivel("MAESTRO_HELADERO")).thenReturn(List.of(response3));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/buscar/nivel")
                            .param("nivel", "MAESTRO_HELADERO"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nivel", is("MAESTRO_HELADERO")));
        }

        @Test
        @DisplayName("Debería retornar 400 con nivel inválido")
        void deberiaRetornar400ConNivelInvalido() throws Exception {
            // GIVEN
            when(fidelizacionService.buscarPorNivel("NIVEL_RARO"))
                    .thenThrow(new BadRequestException("Nivel inválido: NIVEL_RARO"));

            // WHEN / THEN
            mockMvc.perform(get("/api/fidelizacion/buscar/nivel")
                            .param("nivel", "NIVEL_RARO"))
                    .andExpect(status().isBadRequest());
        }
    }

    // DELETE /api/fidelizacion/{id}
    @Nested
    @DisplayName("DELETE /api/fidelizacion/{id}")
    class EliminarFidelizacion {

        @Test
        @DisplayName("Debería eliminar ficha existente y retornar 204 NO CONTENT")
        void deberiaEliminarFicha() throws Exception {
            // GIVEN
            doNothing().when(fidelizacionService).eliminar(1L);

            // WHEN / THEN
            mockMvc.perform(delete("/api/fidelizacion/1"))
                    .andExpect(status().isNoContent());

            verify(fidelizacionService, times(1)).eliminar(1L);
        }

        @Test
        @DisplayName("Debería retornar 404 al eliminar ID inexistente")
        void deberiaRetornar404AlEliminar() throws Exception {
            // GIVEN
            doThrow(new ResourceNotFoundException("Ficha de fidelización no encontrada con ID: 99"))
                    .when(fidelizacionService).eliminar(99L);

            // WHEN / THEN
            mockMvc.perform(delete("/api/fidelizacion/99"))
                    .andExpect(status().isNotFound());
        }
    }
}