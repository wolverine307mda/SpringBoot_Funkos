package org.example.springboot_funkos.rest.pedidos.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.funkos.repositories.FunkoRepository;
import org.example.springboot_funkos.rest.pedidos.exceptions.*;
import org.example.springboot_funkos.rest.pedidos.models.LineaPedido;
import org.example.springboot_funkos.rest.pedidos.models.Pedido;
import org.example.springboot_funkos.rest.pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@CacheConfig(cacheNames = {"pedidos"})
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final FunkoRepository funkoRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, FunkoRepository funkoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.funkoRepository = funkoRepository;
    }

    @Override
    public Page<Pedido> getAll(Pageable pageable) {
        log.info("Obteniendo todos los pedidos paginados...");
        return pedidoRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#idPedido")
    public Pedido getById(ObjectId idPedido) {
        log.info("Obteniendo pedido por su id...");
        return pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
    }

    @Override
    public Page<Pedido> getPedidoByUsuarioId(Long idUsuario, Pageable pageable) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario + "...");
        return pedidoRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido: " + pedido + "...");
        checkPedido(pedido);
        reservarPedido(pedido);
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setUpdatedAt(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#idPedido")
    public void delete(ObjectId idPedido) {
        log.info("Eliminando pedido: " + idPedido + "...");
        var pedidoDeleted = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
        returnStockPedidos(pedidoDeleted);
        pedidoRepository.deleteById(idPedido);
    }

    @Override
    @Transactional
    @CachePut(key = "#idPedido")
    public Pedido update(ObjectId idPedido, Pedido pedido) {
        log.info("Actualizando pedido con id: " + idPedido + "...");
        pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
        returnStockPedidos(pedido);
        checkPedido(pedido);
        var pedidoToSave = reservarPedido(pedido);
        pedidoToSave.setUpdatedAt(LocalDateTime.now());
        return pedidoRepository.save(pedidoToSave);
    }

    private void checkPedido(Pedido pedido) {
        log.info("Comprobando pedido");
        if (pedido.getLineaPedido() == null || pedido.getLineaPedido().isEmpty()) {
            throw new PedidoNotItems(pedido.get_id());
        }
        pedido.getLineaPedido().forEach(lineaPedido -> {
            var funko = funkoRepository.findById(lineaPedido.getIdFunko())
                    .orElseThrow(() -> new FunkoNotFound(lineaPedido.getIdFunko()));
            if (funko.getStock() < lineaPedido.getCantidad() && lineaPedido.getCantidad() > 0) {
                throw new FunkoNotStock(lineaPedido.getIdFunko());
            }
            if (!funko.getPrecio().equals(lineaPedido.getPrecioUnitario())){
                throw new FunkoBadPrice(lineaPedido.getIdFunko());
            }
        });
    }

    private Pedido reservarPedido(Pedido pedido) {
        log.info("Reservando pedido");
        if (pedido.getLineaPedido() == null || pedido.getLineaPedido().isEmpty()) {
            throw new PedidoNotItems(pedido.get_id());
        }
        pedido.getLineaPedido().forEach(lineaPedido -> {
            var funko = funkoRepository.findById(lineaPedido.getIdFunko()).get();
            funko.setStock(funko.getStock() - lineaPedido.getCantidad());
            funkoRepository.save(funko);
            lineaPedido.setTotal(lineaPedido.getCantidad() * lineaPedido.getPrecioUnitario());
        });

        var total = pedido.getLineaPedido().stream()
                .map(lineaPedido -> lineaPedido.getCantidad() * lineaPedido.getPrecioUnitario())
                .reduce(0.0, Double::sum);

        var totalItems = pedido.getLineaPedido().stream()
                .map(LineaPedido::getCantidad)
                .reduce(0, Integer::sum);

        pedido.setTotal(total);
        pedido.setTotalItems(totalItems);

        return pedido;
    }

    Pedido returnStockPedidos(Pedido pedido) {
        log.info("Retornando stock del pedido: {}", pedido);
        if (pedido.getLineaPedido() != null) {
            pedido.getLineaPedido().forEach(lineaPedido -> {
                var funko = funkoRepository.findById(lineaPedido.getIdFunko()).get();
                funko.setStock(funko.getStock() + lineaPedido.getCantidad());
                funkoRepository.save(funko);
            });
        }
        return pedido;
    }
}
