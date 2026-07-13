package com.example.posventa;

import com.example.posventa.model.Posventa;
import com.example.posventa.repository.PosventaRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private static final List<String> ESTADOS_VALIDOS =
            List.of("PENDIENTE", "EN_REVISION", "RESUELTO", "RECHAZADO");

    private final PosventaRepository repository;

    public DataLoader(PosventaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("DataLoader: Base de datos vacía. Generando registros semilla con DataFaker...");
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Posventa posventa = Posventa.builder()
                        .idCliente(faker.number().numberBetween(1L, 100L))
                        .idCompra(faker.number().numberBetween(1000L, 2000L))
                        .motivo(faker.lorem().sentence(10))
                        .estado(ESTADOS_VALIDOS.get(faker.number().numberBetween(0, ESTADOS_VALIDOS.size())))
                        .build();
                repository.save(posventa);
            }
            log.info("DataLoader: 5 posventas de prueba insertadas con éxito!");
        }
    }
}