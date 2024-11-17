package org.example.springboot_funkos.rest.categoria.services;

import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.mappers.CategoriaMapper;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.categoria.repositories.CategoriaRepository;
import org.example.springboot_funkos.rest.categoria.validator.CategoriaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"categorias"})
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;
    private final CategoriaValidator validator;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository repository, CategoriaMapper mapper, CategoriaValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Page<Categoria> getAll(Pageable pageable) {
        // MongoDB soporta paginación con Spring Data
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable
    public Categoria getById(String id) {
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La id no es válida. Debe ser un UUID");
        }
        return repository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría con id " + id + " no se ha encontrado.")
        );
    }

    @Override
    @Cacheable
    public Categoria getByNombre(String nombre) {
        return repository.findByNombre(nombre).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría " + nombre + " no existe.")
        );
    }

    @Override
    @CachePut
    public Categoria save(CategoriaDto categoriaDto) {
        if (!validator.isNameUnique(categoriaDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoría no es válido.");
        }
        return repository.save(mapper.toCategoria(categoriaDto));
    }

    @Override
    @CachePut
    public Categoria update(String id, CategoriaDto categoriaDto) {
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La id no es válida. Debe ser un UUID");
        }
        var categoria = repository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría con id " + id + " no se ha encontrado.")
        );
        if (!validator.isNameUnique(categoriaDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoría ya existe.");
        }
        return repository.save(mapper.toCategoriaUpdate(categoriaDto, categoria));
    }

    @Override
    @CachePut
    public Categoria delete(String id, CategoriaDto categoriaDto) {
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La id no es válida. Debe ser un UUID");
        }
        var categoria = repository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría con id " + id + " no se ha encontrado.")
        );

        // Marcamos la categoría como desactivada
        categoria.setActivado(false);
        return repository.save(categoria);
    }
}
