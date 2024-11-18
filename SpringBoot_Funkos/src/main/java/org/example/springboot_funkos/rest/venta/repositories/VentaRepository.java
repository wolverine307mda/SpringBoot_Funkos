package org.example.springboot_funkos.rest.venta.repositories;

import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VentaRepository extends MongoRepository<Venta, ObjectId> {

    Page<Venta> findByIdUsuario(Long idUsuario, Pageable pageable);

    // Solo queremos el id del pedido dado el id del usuario
    List<Venta> findVentaIdsByIdUsuario(Long idUsuario);

    // existe un producto con el mismo id de Usuario
    boolean existsByIdUsuario(Long idUsuario);
}