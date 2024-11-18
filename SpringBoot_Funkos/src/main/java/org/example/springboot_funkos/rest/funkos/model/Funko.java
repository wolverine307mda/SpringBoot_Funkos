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

@Document(collection = "funkos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funko {

    @Id
    private String id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Min(value = 0)
    @Max(value = 50)
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;

    @DBRef
    private Categoria categoria;

    @CreatedDate
    private LocalDateTime createdAt = truncateToMillis(LocalDateTime.now());

    @LastModifiedDate
    private LocalDateTime updatedAt = truncateToMillis(LocalDateTime.now());

    private LocalDateTime truncateToMillis(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null; // Retorna null si no hay valor
        }
        return dateTime.withNano((dateTime.getNano() / 1_000_000) * 1_000_000);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = truncateToMillis(createdAt);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = truncateToMillis(updatedAt);
    }
}
