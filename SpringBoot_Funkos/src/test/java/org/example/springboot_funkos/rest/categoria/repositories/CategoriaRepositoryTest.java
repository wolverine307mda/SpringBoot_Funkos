package org.example.springboot_funkos.rest.categoria.repositories;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(CategoriaRepository.class)  // Importa solo los beans relacionados con MongoDB
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository repository;

    private Categoria categoriaTest;

    @BeforeEach
    void setUp() {
        categoriaTest = new Categoria();
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("DISNEY");
        categoriaTest.setActivado(true);
        repository.save(categoriaTest); // Guardamos directamente usando el repositorio
    }


    @Test
    void findById() {
        var result = repository.findById(categoriaTest.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> {
                    if (result.isPresent()) {
                        assertEquals("DISNEY", result.get().getNombre());
                        assertTrue(result.get().getActivado());
                    }
                }
        );
    }

    @Test
    void findByIdNotFound() {
        var result = repository.findById(UUID.randomUUID());

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void findByIdAndActivadoTrue() {
        var result = repository.findByIdAndActivadoTrue(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));

        assertAll(
                () -> assertNotNull(result),
                () -> {
                    if (result.isPresent()) {
                        assertEquals("DISNEY", result.get().getNombre());
                        assertTrue(result.get().getActivado());
                    }
                }
        );
    }

    @Test
    void findByIdAndActivadoTrueNotFound() {
        var result = repository.findByIdAndActivadoTrue(UUID.randomUUID());

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void findByNombre() {
        var result = repository.findByNombre("DISNEY");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.get().getNombre()),
                () -> assertTrue(result.get().getActivado())
        );
    }

    @Test
    void findByNombreNotFound() {
        var result = repository.findByNombre("CategoriaTestNotFound");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void saveCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setNombre("TOYS");
        categoria.setActivado(true);

        Categoria savedCategoria = repository.save(categoria);

        assertAll(
                () -> assertNotNull(savedCategoria),
                () -> assertEquals(categoria.getNombre(), savedCategoria.getNombre()),
                () -> assertTrue(savedCategoria.getActivado())
        );
    }

    @Test
    void deleteCategoria() {
        UUID categoriaId = categoriaTest.getId();
        repository.delete(categoriaTest);

        // Verificamos si la categoría fue eliminada
        Optional<Categoria> result = repository.findById(categoriaId);

        assertTrue(result.isEmpty(), "La categoría debería haber sido eliminada");
    }

}
