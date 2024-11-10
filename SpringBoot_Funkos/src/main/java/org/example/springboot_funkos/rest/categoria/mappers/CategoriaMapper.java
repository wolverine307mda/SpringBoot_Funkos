package org.example.springboot_funkos.rest.categoria.mappers;


import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoriaMapper {
    public Categoria toCategoria(CategoriaDto categoriaDto) {
        var categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        return categoria;
    }

    public Categoria toCategoriaUpdate(CategoriaDto categoriaDto, Categoria categoria){
        return new Categoria(
                categoria.getId(),
                categoriaDto.getNombre() != null ? categoriaDto.getNombre() : categoria.getNombre(),
                categoria.getCreatedAt(),
                LocalDateTime.now(),
                categoriaDto.getActivado() != null ? categoriaDto.getActivado() : categoria.getActivado()
        );
    }
}
