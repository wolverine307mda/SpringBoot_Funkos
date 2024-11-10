package org.example.springboot_funkos.rest.categoria.services;

import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoriaService {
    Page<Categoria> getAll(Pageable pageable);
    Categoria getById(String id);
    Categoria getByNombre(String nombre);
    Categoria save(CategoriaDto categoriaDto);
    Categoria update(String id, CategoriaDto categoriaDto);
    Categoria delete(String id, CategoriaDto categoriaDto);
}