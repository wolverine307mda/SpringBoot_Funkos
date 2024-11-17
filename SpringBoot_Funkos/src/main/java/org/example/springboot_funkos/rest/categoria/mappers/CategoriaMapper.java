package org.example.springboot_funkos.rest.categoria.mappers;

import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CategoriaMapper {

    public Categoria toCategoria(CategoriaDto categoriaDto) {
        Categoria categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setCreatedAt(LocalDateTime.now());
        categoria.setUpdatedAt(LocalDateTime.now());
        categoria.setActivado(categoriaDto.getActivado() != null ? categoriaDto.getActivado() : true);
        return categoria;
    }

    public Categoria toCategoriaUpdate(CategoriaDto categoriaDto, Categoria categoria) {
        return new Categoria(
                categoria.getId(),
                categoriaDto.getNombre() != null ? categoriaDto.getNombre() : categoria.getNombre(),
                categoria.getCreatedAt(),
                LocalDateTime.now(),
                categoriaDto.getActivado() != null ? categoriaDto.getActivado() : categoria.getActivado()
        );
    }

    public CategoriaDto toCategoriaDto(Categoria categoria) {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setId(categoria.getId());
        categoriaDto.setNombre(categoria.getNombre());
        categoriaDto.setActivado(categoria.getActivado());
        return categoriaDto;
    }
}
