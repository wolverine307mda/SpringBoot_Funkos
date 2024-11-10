package org.example.springboot_funkos.rest.pedidos.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record Cliente(
        @Length(min = 2, message = "El nombre debe tener como minimo 2 caracteres")
        String nombre,

        @Email(message = "El email debe ser valido")
        String email,

        @NotBlank(message = "El telefono no puede estar vacio")
        String telefono,

        @NotNull(message = "La direccion no puede ser nula")
        Direccion direccion
) {
}
