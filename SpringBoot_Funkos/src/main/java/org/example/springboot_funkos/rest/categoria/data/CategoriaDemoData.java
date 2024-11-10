/*package org.example.springboot_funkos.categoria.data;

import org.example.springboot_funkos.categoria.model.Categoria;
import org.example.springboot_funkos.categoria.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1) // Se ejecuta primero
public class CategoriaDemoData {

    @Bean
    public CommandLineRunner cargarDatosDemo(CategoriaRepository categoriaRepository) {
        return args -> {
            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new Categoria(null, "SERIE", null, null, true, null));
                categoriaRepository.save(new Categoria(null, "DISNEY", null, null, true, null));
                categoriaRepository.save(new Categoria(null, "SUPERHEROES", null, null, true, null));
                categoriaRepository.save(new Categoria(null, "PELICULA", null, null, true, null));
                categoriaRepository.save(new Categoria(null, "OTROS", null, null, true, null));
            }
        };
    }
}
*/