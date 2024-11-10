package org.example.springboot_funkos.rest.funkos.model;

import jakarta.persistence.*;
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

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funko {
    private static final Long DEFAULT_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id=DEFAULT_ID;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Column(name = "precio", nullable = false)
    @Min(value = 0)
    @Max(value = 50)
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(columnDefinition = "integer default 0")
    private Integer stock = 0;

    @ManyToOne
    @JoinColumn(name = "categoria")
    private Categoria categoria;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();
}
