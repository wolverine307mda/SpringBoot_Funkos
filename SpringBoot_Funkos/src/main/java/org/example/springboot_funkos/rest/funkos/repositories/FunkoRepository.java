package org.example.springboot_funkos.rest.funkos.repositories;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunkoRepository extends JpaRepository<Funko, String> {
    @NotNull Optional<Funko> findById(String id);
    Optional<Funko> findByNombre(String nombre);
    Optional<Funko> findByStock(Integer stock);
}