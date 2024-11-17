package org.example.springboot_funkos.rest.funkos.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document(collection = "funkos")  // MongoDB collections are used instead of JPA tables
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funko {
    private static final Long DEFAULT_ID = 0L;

    @Id  // MongoDB uses @Id instead of @GeneratedValue for unique identifiers
    private String id;  // MongoDB uses String as the default ID type

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Min(value = 0)
    @Max(value = 50)
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;

    @DBRef  // MongoDB uses DBRef to reference other documents like `Categoria`
    private Categoria categoria;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();
}
