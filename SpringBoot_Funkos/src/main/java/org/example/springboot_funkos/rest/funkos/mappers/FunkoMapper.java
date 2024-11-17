package org.example.springboot_funkos.rest.funkos.mappers;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.funkos.dto.FunkoDto;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class FunkoMapper {
    // Este método ahora acepta un nombre de categoría y lo convierte en un objeto Categoria
    public Funko toFunko(FunkoDto funkoDto, Categoria categoria) {
        return new Funko(
                null,  // En MongoDB, no es necesario generar un ID explícitamente si usas un ObjectId.
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                funkoDto.getStock(),
                categoria,  // Aquí ya se pasa un objeto Categoria completo
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // Si en algún momento necesitas un mapper inverso para convertir Funko a DTO:
    public FunkoDto toDto(Funko funko) {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre(funko.getNombre());
        funkoDto.setPrecio(funko.getPrecio());
        funkoDto.setStock(funko.getStock());
        funkoDto.setCategoria(funko.getCategoria().getNombre());  // Si quieres pasar el nombre de la categoría
        return funkoDto;
    }
}
