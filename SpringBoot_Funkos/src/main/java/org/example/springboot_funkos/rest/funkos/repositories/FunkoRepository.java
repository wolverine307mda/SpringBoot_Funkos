package org.example.springboot_funkos.rest.funkos.repositories;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long> {
    Optional<Funko> findById(Long id);
    Optional<Funko> findByNombre(String nombre);
    Optional<Funko> findByStock(Integer stock);
}