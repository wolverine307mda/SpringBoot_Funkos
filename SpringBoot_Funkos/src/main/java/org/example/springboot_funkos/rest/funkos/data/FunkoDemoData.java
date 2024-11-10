/*package org.example.springboot_funkos.funkos.data;

import org.example.springboot_funkos.categoria.model.Categoria;
import org.example.springboot_funkos.categoria.service.CategoriaService;
import org.example.springboot_funkos.funkos.model.Funko;
import org.example.springboot_funkos.funkos.repository.FunkoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Order(2) // Se ejecuta después de CategoriaDemoData
public class FunkoDemoData {

    @Bean
    public CommandLineRunner cargarFunkosDemo(FunkoRepository funkoRepository, CategoriaService categoriaService) {
        return args -> {
            if (funkoRepository.count() == 0) {
                LocalDateTime now = LocalDateTime.now();

                // Obtener todas las categorías y mapearlas por nombre
                List<Categoria> categorias = categoriaService.getAll();
                Map<String, Categoria> categoriaMap = categorias.stream()
                        .collect(Collectors.toMap(Categoria::getName, categoria -> categoria));

                // Verificar que las categorías necesarias estén disponibles
                if (categoriaMap.containsKey("SERIE") && categoriaMap.containsKey("DISNEY")
                        && categoriaMap.containsKey("SUPERHEROES") && categoriaMap.containsKey("PELICULA")
                        && categoriaMap.containsKey("OTROS")) {

                    // Guardar Funkos con las categorías respectivas
                    funkoRepository.save(Funko.builder()
                            .nombre("Eleven (Stranger Things)")
                            .precio(12.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SERIE"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Mickey Mouse")
                            .precio(9.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("DISNEY"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Iron Man")
                            .precio(14.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SUPERHEROES"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Harry Potter")
                            .precio(15.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("PELICULA"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Jack Skellington")
                            .precio(13.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("DISNEY"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Spider-Man")
                            .precio(17.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SUPERHEROES"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Batman")
                            .precio(16.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SUPERHEROES"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Darth Vader")
                            .precio(18.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("PELICULA"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Spongebob")
                            .precio(11.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SERIE"))
                            .build());

                    funkoRepository.save(Funko.builder()
                            .nombre("Deadpool")
                            .precio(14.99)
                            .createdAt(now)
                            .updatedAt(now)
                            .categoria(categoriaMap.get("SUPERHEROES"))
                            .build());

                } else {
                    System.out.println("Algunas categorías necesarias no están disponibles.");
                }
            }
        };
    }
}
*/