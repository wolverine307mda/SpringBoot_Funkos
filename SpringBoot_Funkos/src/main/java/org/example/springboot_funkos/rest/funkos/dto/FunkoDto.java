package org.example.springboot_funkos.rest.funkos.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FunkoDto {
        @NotBlank(message = "El nombre no puede estar vacio")
        private String nombre;

        @Min(value = 0)
        @Max(value = 50)
        @NotNull(message = "El precio no puede ser un campo nulo")
        private Double precio;

        @NotNull(message = "El stock no puede ser un campo nulo")
        private Integer stock;

        @NotBlank(message = "La categoria no puede estar vacia")
        private String categoria;  // Aquí se mantiene como String, ya que puedes utilizar un ID o nombre de categoría para mapear.

        // Si se necesita, puedes manejar que el stock sea cero cuando no se pase el valor:
        public void setStock(Integer stock) {
                this.stock = (stock == null) ? 0 : stock;
        }
}
