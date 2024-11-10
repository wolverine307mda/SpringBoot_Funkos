package org.example.springboot_funkos.rest.categoria.validator;

import org.example.springboot_funkos.rest.categoria.repositories.CategoriaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoriaValidator {
    private final CategoriaRepository categoriaRepository;

    public CategoriaValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public boolean isNameUnique(String nombre) {
        return categoriaRepository.findByNombre(nombre).isEmpty();
    }

    public boolean isIdValid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}