package org.example.springboot_funkos.rest.venta.services;


import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaService {
    Page<Venta> getAll(Pageable pageable);
    Venta getById(ObjectId idPedido);
    Page<Venta> getPedidoByUsuarioId(Long idUsuario, Pageable pageable);
    Venta save(Venta venta);
    void delete(ObjectId idPedido);
    Venta update(ObjectId idPedido, Venta venta);
}