package org.example.springboot_funkos.rest.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoriaDto {
    UUID id;
    @NotBlank(message = "El nombre no puede estar vacio")
    String nombre;
    Boolean activado;
}
