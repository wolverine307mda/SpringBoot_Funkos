package org.example.springboot_funkos.rest.categoria.repositories;

import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, UUID> {
    Optional<Categoria> findByIdAndActivadoTrue(UUID id);
    Optional<Categoria> findByNombre(String nombre);
}

