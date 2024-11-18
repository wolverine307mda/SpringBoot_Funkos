package org.example.springboot_funkos.rest.funkos.repositories;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.categoria.repositories.CategoriaRepository;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class FunkoRepositoryTest {

    @Autowired
    private FunkoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @DBRef
    private Categoria categoria;

    // Truncar fechas al inicializar objetos
    private static final Categoria categoriaTest = new Categoria(
            UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"),
            "PELICULA",
            truncateToMillis(LocalDateTime.now()),
            truncateToMillis(LocalDateTime.now()),
            true
    );

    private static Funko funkoTest = new Funko(
            "1",
            "Darth Vader",
            10.99,
            20,
            categoriaTest,
            truncateToMillis(LocalDateTime.now()),
            truncateToMillis(LocalDateTime.now())
    );

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Limpia FunkoRepository
        categoriaRepository.deleteAll(); // Limpia CategoriaRepository

        // Persistir la categoría antes de asociarla
        Categoria categoriaPersistida = categoriaRepository.save(categoriaTest);

        // Configurar Funko con la categoría persistida
        funkoTest.setCategoria(categoriaPersistida);
        funkoTest.setCreatedAt(truncateToMillis(funkoTest.getCreatedAt()));
        funkoTest.setUpdatedAt(truncateToMillis(funkoTest.getUpdatedAt()));

        funkoTest = repository.save(funkoTest); // Persistir Funko
    }

    @Test
    void findAll() {
        var result = repository.findAll();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(funkoTest.getNombre(), result.get(0).getNombre()),
                () -> assertEquals(funkoTest.getPrecio(), result.get(0).getPrecio()),
                () -> assertEquals(funkoTest.getStock(), result.get(0).getStock()),
                () -> assertEquals(funkoTest.getCategoria(), result.get(0).getCategoria())
        );
    }

    @Test
    void findById() {
        String id = funkoTest.getId();

        var result = repository.findById(id);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(funkoTest.getNombre(), result.get().getNombre()),
                () -> assertEquals(funkoTest.getPrecio(), result.get().getPrecio()),
                () -> assertEquals(funkoTest.getStock(), result.get().getStock()),
                () -> assertEquals(funkoTest.getCategoria(), result.get().getCategoria())
        );
    }

    @Test
    void findByIdNotFound() {
        var result = repository.findById("999");

        assertTrue(result.isEmpty());
    }

    @Test
    void save() {
        var newFunko = new Funko(
                "2",
                "Luke Skywalker",
                12.99,
                15,
                categoriaTest,
                truncateToMillis(LocalDateTime.now()),
                truncateToMillis(LocalDateTime.now())
        );

        var savedFunko = repository.save(newFunko);

        assertAll(
                () -> assertNotNull(savedFunko.getId()),
                () -> assertEquals(newFunko.getNombre(), savedFunko.getNombre()),
                () -> assertEquals(newFunko.getPrecio(), savedFunko.getPrecio()),
                () -> assertEquals(newFunko.getStock(), savedFunko.getStock()),
                () -> assertEquals(newFunko.getCategoria(), savedFunko.getCategoria())
        );
    }

    @Test
    void findByNombre() {
        var result = repository.findByNombre("Darth Vader");

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(funkoTest.getNombre(), result.get().getNombre()),
                () -> assertEquals(funkoTest.getPrecio(), result.get().getPrecio()),
                () -> assertEquals(funkoTest.getStock(), result.get().getStock()),
                () -> assertEquals(funkoTest.getCategoria(), result.get().getCategoria())
        );
    }

    @Test
    void findByNombreNotFound() {
        var result = repository.findByNombre("FunkoTestNotFound");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByStock() {
        var result = repository.findByStock(20);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(funkoTest.getNombre(), result.get().getNombre()),
                () -> assertEquals(funkoTest.getPrecio(), result.get().getPrecio()),
                () -> assertEquals(funkoTest.getStock(), result.get().getStock()),
                () -> assertEquals(funkoTest.getCategoria(), result.get().getCategoria())
        );
    }

    @Test
    void findByStockNotFound() {
        var result = repository.findByStock(3000);

        assertTrue(result.isEmpty());
    }

    // Método utilitario para truncar fechas a milisegundos
    private static LocalDateTime truncateToMillis(LocalDateTime dateTime) {
        return dateTime.withNano((dateTime.getNano() / 1_000_000) * 1_000_000);
    }
}
