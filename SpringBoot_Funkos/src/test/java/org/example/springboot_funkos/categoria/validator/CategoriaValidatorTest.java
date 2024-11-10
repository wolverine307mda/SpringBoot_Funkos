package org.example.springboot_funkos.categoria.validator;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.categoria.repositories.CategoriaRepository;
import org.example.springboot_funkos.rest.categoria.validator.CategoriaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaValidatorTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaValidator categoriaValidator;

    @Test
    void isNameUnique() {
        String nombre = "CategoriaNueva";

        when(repository.findByNombre(nombre)).thenReturn(Optional.empty());

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertTrue(result);

        verify(repository, times(1)).findByNombre(nombre);
    }

    @Test
    void isNameUniqueFalse() {
        String nombre = "CategoriaExistente";

        when(repository.findByNombre(nombre)).thenReturn(Optional.of(new Categoria()));

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertFalse(result);

        verify(repository, times(1)).findByNombre(nombre);
    }

    @Test
    void isIdValid() {
        String idValida = "4182d617-ec89-4fbc-be95-85e461778766";

        boolean result = categoriaValidator.isIdValid(idValida);

        assertTrue(result);
    }

    @Test
    void isIdInvalid() {
        String idInvalida = "4182d617-ec89-4f";

        boolean result = categoriaValidator.isIdValid(idInvalida);

        assertFalse(result);
    }
}