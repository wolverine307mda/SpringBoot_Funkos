package org.example.springboot_funkos.rest.funkos.mappers;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.funkos.dto.FunkoDto;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FunkoMapper {
    public Funko toFunko(FunkoDto funkoDto, Categoria categoria) {
        return new Funko(
                null,
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                funkoDto.getStock(),
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Funko toFunkoUpdate(FunkoDto funkoDto, Funko funko, Categoria categoria){
        return new Funko(
                funko.getId(),
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                funkoDto.getStock(),
                categoria,
                funko.getCreatedAt(),
                LocalDateTime.now()
        );
    }
}
