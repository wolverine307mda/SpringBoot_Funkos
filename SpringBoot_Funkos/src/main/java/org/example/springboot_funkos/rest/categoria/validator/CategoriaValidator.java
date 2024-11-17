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

    /**
     * Verifica si el nombre de la categoría es único.
     *
     * @param nombre el nombre de la categoría
     * @return true si el nombre es único, false en caso contrario
     */
    public boolean isNameUnique(String nombre) {
        return categoriaRepository.findByNombre(nombre).isEmpty();
    }

    /**
     * Valida si una cadena representa un UUID válido.
     *
     * @param value la cadena a validar
     * @return true si la cadena es un UUID válido, false en caso contrario
     */
    public boolean isIdValid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
