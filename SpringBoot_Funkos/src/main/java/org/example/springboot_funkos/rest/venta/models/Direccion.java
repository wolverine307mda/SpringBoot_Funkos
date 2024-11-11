package org.example.springboot_funkos.rest.venta.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record Direccion(
        @Length(min = 2, message = "La calle debe tener 2 caracteres como minimo")
        String calle,

        @NotBlank(message = "El numero no puede estar vacio")
        String numero,

        @Length(min = 2, message = "El nombre de la ciudad debe tener 2 caracteres como minimo")
        String ciudad,

        @Length(min = 2, message = "El nombre de la provincia debe tener 2 caracteres como minimo")
        String provincia,

        @Length(min = 2, message = "El nombre del pais debe tener 2 caracteres como minimo")
        String pais,

        @NotBlank(message = "El codigo postal no puede estar vacio")
        @Pattern(regexp = "^[0-9]{5}$", message = "El codigo postal deben ser 5 digitos")
        String codigoPostal
) {
}
