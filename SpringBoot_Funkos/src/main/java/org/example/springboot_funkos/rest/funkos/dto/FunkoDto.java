package org.example.springboot_funkos.rest.funkos.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FunkoDto {
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre;

        @Min(value = 0)
        @Max(value = 50)
        @NotNull(message = "El precio no puede ser un campo nulo")
        Double precio;

        @NotNull(message = "El stock no puede ser un campo nulo")
        private Integer stock;

        @NotBlank(message = "La categoria no puede estar vacia")
        String categoria;

        public void setStock(Integer stock) {
                this.stock = (stock == null) ? 0 : stock;
        }

}
