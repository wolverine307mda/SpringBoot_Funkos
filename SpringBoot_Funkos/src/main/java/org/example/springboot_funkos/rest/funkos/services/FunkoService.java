package org.example.springboot_funkos.rest.funkos.services;

import org.example.springboot_funkos.rest.funkos.dto.FunkoDto;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface FunkoService {
    Page<Funko> getAll(Pageable pageable);
    Funko getById(String id);
    Funko getByNombre(String nombre);
    Optional<Funko> getByStock(Integer stock);
    Funko save(FunkoDto funkoDto);
    Funko update(String id, FunkoDto funkoDto);
    Funko delete(String id);
}
