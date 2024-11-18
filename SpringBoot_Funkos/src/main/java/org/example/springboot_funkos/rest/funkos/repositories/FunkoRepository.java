package org.example.springboot_funkos.rest.funkos.repositories;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunkoRepository extends MongoRepository<Funko, String> {
    @NotNull Optional<Funko> findById(String id);
    Optional<Funko> findByNombre(String nombre);
    Optional<Funko> findByStock(Integer stock);
}
