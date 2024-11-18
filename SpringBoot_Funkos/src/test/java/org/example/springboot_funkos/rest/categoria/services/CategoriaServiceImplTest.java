package org.example.springboot_funkos.rest.categoria.services;

import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.mappers.CategoriaMapper;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.categoria.repositories.CategoriaRepository;
import org.example.springboot_funkos.rest.categoria.services.CategoriaServiceImpl;
import org.example.springboot_funkos.rest.categoria.validator.CategoriaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private CategoriaValidator validator;

    @InjectMocks
    private CategoriaServiceImpl service;

    private Categoria categoriaTest;

    @BeforeEach
    void setUp() {
        categoriaTest = new Categoria();
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("DISNEY");
        categoriaTest.setActivado(true);
    }

    @Test
    void getAlltest() {
        Categoria categoriaTest = new Categoria();
        categoriaTest.setNombre("DISNEY");
        categoriaTest.setActivado(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Categoria> page = new PageImpl<>(List.of(categoriaTest));

        when(repository.findAll(pageable)).thenReturn(page);

        var result = service.getAll(pageable);

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertTrue(result.getContent().contains(categoriaTest)),
                () -> assertEquals("DISNEY", result.getContent().get(0).getNombre()),
                () -> assertTrue(result.getContent().get(0).getActivado())
        );

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void getById() {
        when(validator.isIdValid("12d45756-3895-49b2-90d3-c4a12d5ee081")).thenReturn(true);
        when(repository.findById(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"))).thenReturn(Optional.of(categoriaTest));

        var result = service.getById("12d45756-3895-49b2-90d3-c4a12d5ee081");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(validator, times(1)).isIdValid("12d45756-3895-49b2-90d3-c4a12d5ee081");
        verify(repository, times(1)).findById(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
    }

    @Test
    void getByIdNotFound() {
        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778700")).thenReturn(true);
        when(repository.findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"))).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getById("4182d617-ec89-4fbc-be95-85e461778700")
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoría con id 4182d617-ec89-4fbc-be95-85e461778700 no se ha encontrado.", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778700");
        verify(repository, times(1)).findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"));
    }

    @Test
    void getByIdNotValid() {
        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getById("4182d617-ec89-4f")
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
    }

    @Test
    void getByNombre() {
        when(repository.findByNombre("DISNEY")).thenReturn(Optional.ofNullable(categoriaTest));

        var result = service.getByNombre("DISNEY");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findByNombre("DISNEY");
    }

    @Test
    void getByNombreNotFound() {
        when(repository.findByNombre("CategoriaTestNotFound")).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getByNombre("CategoriaTestNotFound")
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoría CategoriaTestNotFound no existe.", thrown.getReason());

        verify(repository, times(1)).findByNombre("CategoriaTestNotFound");
    }

    @Test
    void save() {
        CategoriaDto nuevaCategoria = new CategoriaDto();
        nuevaCategoria.setNombre("DISNEY");
        nuevaCategoria.setActivado(true);

        Categoria categoria = new Categoria();
        categoria.setNombre(nuevaCategoria.getNombre());
        categoria.setActivado(nuevaCategoria.getActivado());

        when(validator.isNameUnique(nuevaCategoria.getNombre())).thenReturn(true);
        when(mapper.toCategoria(nuevaCategoria)).thenReturn(categoria);
        when(repository.save(categoria)).thenReturn(categoria);

        var result = service.save(nuevaCategoria);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).save(categoria);
        verify(mapper, times(1)).toCategoria(nuevaCategoria);
    }

    @Test
    void saveInvalidName() {
        CategoriaDto nuevaCategoriaDto = new CategoriaDto();
        nuevaCategoriaDto.setNombre("CategoriaTest");
        nuevaCategoriaDto.setActivado(true);

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("CategoriaTest");
        nuevaCategoria.setActivado(true);

        when(validator.isNameUnique("CategoriaTest")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.save(nuevaCategoriaDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("El nombre de la categoría no es válido.", thrown.getReason());

        verify(validator, times(1)).isNameUnique(nuevaCategoriaDto.getNombre());
    }

    @Test
    void update() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("CategoriaTest");
        categoriaExistente.setActivado(true);

        Categoria categoriaUpdate = new Categoria();
        categoriaUpdate.setId(id);
        categoriaUpdate.setNombre("CategoriaTestUpdate");
        categoriaUpdate.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(validator.isNameUnique("CategoriaTestUpdate")).thenReturn(true);
        when(mapper.toCategoriaUpdate(categoriaUpdateDto, categoriaExistente)).thenReturn(categoriaUpdate);
        when(repository.save(categoriaUpdate)).thenReturn(categoriaUpdate);

        var result = service.update(id.toString(), categoriaUpdateDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("CategoriaTestUpdate", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(categoriaUpdate);
        verify(mapper, times(1)).toCategoriaUpdate(categoriaUpdateDto, categoriaExistente);
        verify(validator, times(1)).isNameUnique("CategoriaTestUpdate");
    }

    @Test
    void updateNotValidId() {
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update("4182d617-ec89-4f", categoriaUpdateDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
    }

    @Test
    void updateNotFound() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778700")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update("4182d617-ec89-4fbc-be95-85e461778700", categoriaUpdateDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoría con id 4182d617-ec89-4fbc-be95-85e461778700 no se ha encontrado.", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778700");
        verify(repository, times(1)).findById(id);
    }

    @Test
    void delete() {
        // ID de la categoría
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaBorradaDto = new CategoriaDto();
        categoriaBorradaDto.setNombre("CategoriaTest");
        categoriaBorradaDto.setActivado(true);

        // Categoría existente con el id
        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("CategoriaTest");
        categoriaExistente.setActivado(true);

        // Categoría que se borrará (se desactivará)
        Categoria categoriaBorrada = new Categoria();
        categoriaBorrada.setId(id);
        categoriaBorrada.setNombre("CategoriaTest");
        categoriaBorrada.setActivado(false);  // Será desactivada en el proceso de borrado
        categoriaBorrada.setCreatedAt(LocalDateTime.now());  // Establecer la fecha explícitamente
        categoriaBorrada.setUpdatedAt(LocalDateTime.now());  // Establecer la fecha explícitamente

        // Mock para simular la respuesta del repositorio
        when(validator.isIdValid(id.toString())).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(repository.save(categoriaBorrada)).thenReturn(categoriaBorrada);

        // Llamar al servicio delete
        var result = service.delete(id.toString(), categoriaBorradaDto);

        // Verificar que el resultado no sea nulo y contiene los valores esperados
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("CategoriaTest", result.getNombre()),
                () -> assertFalse(result.getActivado()) // Esperamos que el campo "activado" sea falso
        );

        // Verificar que las interacciones con los mocks ocurrieron como se esperaba
        verify(validator, times(1)).isIdValid(id.toString());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(categoriaBorrada);
    }

    @Test
    void deleteNotValidId() {
        CategoriaDto categoriaBorradaDto = new CategoriaDto();
        categoriaBorradaDto.setNombre("CategoriaTest");
        categoriaBorradaDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.delete("4182d617-ec89-4f", categoriaBorradaDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
    }

    @Test
    void deleteNotFound() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaBorradaDto = new CategoriaDto();
        categoriaBorradaDto.setNombre("CategoriaTest");
        categoriaBorradaDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.empty()); // Simulamos que no se encuentra la categoría

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> service.delete("4182d617-ec89-4fbc-be95-85e461778766", categoriaBorradaDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoría con id 4182d617-ec89-4fbc-be95-85e461778766 no se ha encontrado.", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findById(id); // Verificamos que se llama a findById
    }

}