package com.bravatta.envios;

import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import com.bravatta.envios.repository.EnviosRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final EnviosRepository repository;

    public DataLoader(EnviosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("DataLoader: Base de datos vacía. Generando registros semilla con DataFaker...");
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Envios d = Envios.builder()
                        .idCompra(faker.number().numberBetween(1000L, 2000L))
                        .direccionEntrega(faker.address().streetAddress())
                        .estadoEnvio(EstadoEnvio.PENDIENTE)
                        .repartidorAsignado(faker.name().fullName())
                        .fechaEstimadaEntrega(LocalDate.now().plusDays(faker.number().numberBetween(1, 5)))
                        .fechaRegistro(LocalDateTime.now())
                        .build();
                repository.save(d);
            }
            log.info("DataLoader: ¡5 deliveries de prueba insertados con éxito!");
        }
    }
}