package org.example.springboot_funkos.rest.venta.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("pedidos")
public class Venta {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long idUsuario;

    @NotNull(message = "El id del cliente no puede ser nulo")
    private Cliente cliente;

    @NotNull(message = "Debe haber al menos 1 linea de pedido")
    private List<LineaVenta> lineaVenta;

    @Builder.Default
    private Integer totalItems = 0;

    @Builder.Default
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isDeleted = false;

    @JsonProperty("id")
    public String get_id() { return id.toHexString(); }

    public void setLineasPedido (List<LineaVenta> lineaVenta){
        this.lineaVenta = lineaVenta;
        this.totalItems = lineaVenta !=null ? lineaVenta.size() : 0;
        this.total = lineaVenta !=null ? lineaVenta.stream().mapToDouble(LineaVenta::getTotal).sum() : 0.0;
    }
}