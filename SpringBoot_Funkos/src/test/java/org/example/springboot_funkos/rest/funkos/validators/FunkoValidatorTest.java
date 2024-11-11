package org.example.springboot_funkos.rest.funkos.validators;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.rest.funkos.repositories.FunkoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FunkoValidatorTest {

    @Mock
    private FunkoRepository funkoRepository;

    @InjectMocks
    private FunkoValidator funkoValidator;

    @Test
    void isNameUnique_ReturnsTrue_WhenNameIsUnique() {
        // Mocking the behavior of the repository to simulate that no Funko exists with the given name
        when(funkoRepository.findByNombre("Darth Vader")).thenReturn(Optional.empty());

        boolean result = funkoValidator.isNameUnique("Darth Vader");

        assertTrue(result, "Expected name to be unique.");
    }

    @Test
    void isNameUnique_ReturnsFalse_WhenNameIsNotUnique() {
        // Mocking the behavior of the repository to simulate that a Funko exists with the given name
        when(funkoRepository.findByNombre("Darth Vader")).thenReturn(Optional.of(new Funko()));

        boolean result = funkoValidator.isNameUnique("Darth Vader");

        assertFalse(result, "Expected name to not be unique.");
    }

    @Test
    void isIdValid_ReturnsTrue_WhenIdIsValid() {
        // Test with a valid numeric string
        boolean result = funkoValidator.isIdValid("12345");

        assertTrue(result, "Expected ID to be valid.");
    }

    @Test
    void isIdValid_ReturnsFalse_WhenIdIsInvalid() {
        // Test with an invalid non-numeric string
        boolean result = funkoValidator.isIdValid("abc123");

        assertFalse(result, "Expected ID to be invalid.");
    }

    @Test
    void isIdValid_ReturnsFalse_WhenIdIsEmpty() {
        // Test with an empty string
        boolean result = funkoValidator.isIdValid("");

        assertFalse(result, "Expected ID to be invalid.");
    }
}
