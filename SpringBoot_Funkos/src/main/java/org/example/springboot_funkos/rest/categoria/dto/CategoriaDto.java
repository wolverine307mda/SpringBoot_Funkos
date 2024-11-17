package org.example.springboot_funkos.rest.categoria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaDto {
    private UUID id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    private Boolean activado;
}
