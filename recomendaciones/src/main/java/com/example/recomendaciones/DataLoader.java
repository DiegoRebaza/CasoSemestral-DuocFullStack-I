package com.example.recomendaciones;

import com.example.recomendaciones.model.Recomendacion;
import com.example.recomendaciones.repository.RecomendacionRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final RecomendacionRepository repository;

    public DataLoader(RecomendacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("DataLoader: Base de datos vacía. Generando registros semilla con DataFaker...");
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Recomendacion rec = Recomendacion.builder()
                        .idCliente(faker.number().numberBetween(1L, 100L))
                        .idProducto(faker.number().numberBetween(1L, 500L))
                        .idCompra(faker.number().numberBetween(1000L, 2000L))
                        .opinionUsuario(faker.lorem().sentence(10))
                        .puntuacionAfinidad(faker.number().randomDouble(1, 1, 5))
                        .fechaCalculo(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 180)))
                        .build();
                repository.save(rec);
            }
            log.info("DataLoader: ¡5 recomendaciones de prueba insertadas con éxito!");
        }
    }
}