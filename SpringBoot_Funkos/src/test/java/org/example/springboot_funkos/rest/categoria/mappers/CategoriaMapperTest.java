package org.example.springboot_funkos.rest.categoria.mappers;

import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaMapperTest {

    private final CategoriaMapper mapper = new CategoriaMapper();

    @Test
    void toCategoria() {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("DISNEY");
        categoriaDto.setActivado(true);

        var res = mapper.toCategoria(categoriaDto);

        assertAll(
                () -> assertEquals(categoriaDto.getNombre(), res.getNombre()),
                () -> assertEquals(categoriaDto.getActivado(), res.getActivado())
        );
    }

    @Test
    void toCategoriaUpdate() {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("DISNEY");
        categoriaDto.setActivado(true);

        Categoria categoria = new Categoria(
                UUID.randomUUID(),
                "MARVEL",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );

        var res = mapper.toCategoriaUpdate(categoriaDto, categoria);

        assertAll(
                () -> assertEquals(categoria.getId(), res.getId()),
                () -> assertEquals(categoriaDto.getNombre(), res.getNombre()),
                () -> assertEquals(categoriaDto.getActivado(), res.getActivado()),
                () -> assertEquals(categoria.getCreatedAt(), res.getCreatedAt(), "La fecha de creación debe mantenerse igual"),
                () -> assertNotNull(res.getUpdatedAt(), "La fecha de actualización debe estar presente")
        );
    }

    @Test
    void toCategoriaUpdateWithNullValues() {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setNombre(null);
        categoriaDto.setActivado(null);

        Categoria categoria = new Categoria(
                UUID.randomUUID(),
                "FUNKO POP",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );

        var res = mapper.toCategoriaUpdate(categoriaDto, categoria);

        assertAll(
                () -> assertEquals(categoria.getId(), res.getId()),
                () -> assertEquals(categoria.getNombre(), res.getNombre(), "El nombre no debe cambiar si es null en el DTO"),
                () -> assertEquals(categoria.getActivado(), res.getActivado(), "El estado de activado no debe cambiar si es null en el DTO"),
                () -> assertEquals(categoria.getCreatedAt(), res.getCreatedAt(), "La fecha de creación debe mantenerse igual"),
                () -> assertNotNull(res.getUpdatedAt(), "La fecha de actualización debe estar presente")
        );
    }

    @Test
    void toCategoriaUpdatePreservesOriginalDataIfDtoIsEmpty() {
        CategoriaDto categoriaDto = new CategoriaDto();

        Categoria categoria = new Categoria(
                UUID.randomUUID(),
                "STAR WARS",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );

        var res = mapper.toCategoriaUpdate(categoriaDto, categoria);

        assertAll(
                () -> assertEquals(categoria.getId(), res.getId()),
                () -> assertEquals(categoria.getNombre(), res.getNombre(), "El nombre original debe mantenerse"),
                () -> assertEquals(categoria.getActivado(), res.getActivado(), "El estado original debe mantenerse"),
                () -> assertEquals(categoria.getCreatedAt(), res.getCreatedAt(), "La fecha de creación debe mantenerse igual")
        );
    }
}
