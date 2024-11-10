package org.example.springboot_funkos.rest.pedidos.services;


import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.pedidos.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {
    Page<Pedido> getAll(Pageable pageable);
    Pedido getById(ObjectId idPedido);
    Page<Pedido> getPedidoByUsuarioId(Long idUsuario, Pageable pageable);
    Pedido save(Pedido pedido);
    void delete(ObjectId idPedido);
    Pedido update(ObjectId idPedido, Pedido pedido);
}