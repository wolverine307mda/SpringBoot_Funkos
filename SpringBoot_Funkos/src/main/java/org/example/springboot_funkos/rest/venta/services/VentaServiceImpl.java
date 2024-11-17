package org.example.springboot_funkos.rest.venta.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.funkos.repositories.FunkoRepository;
import org.example.springboot_funkos.rest.venta.exceptions.*;
import org.example.springboot_funkos.rest.venta.models.LineaVenta;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.example.springboot_funkos.rest.venta.repositories.VentaRepository;
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
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final FunkoRepository funkoRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository, FunkoRepository funkoRepository) {
        this.ventaRepository = ventaRepository;
        this.funkoRepository = funkoRepository;
    }

    @Override
    public Page<Venta> getAll(Pageable pageable) {
        log.info("Obteniendo todos los pedidos paginados...");
        return ventaRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#idPedido")
    public Venta getById(ObjectId idPedido) {
        log.info("Obteniendo pedido por su id...");
        return ventaRepository.findById(idPedido).orElseThrow(() -> new VentaNotFound(idPedido.toHexString()));
    }

    @Override
    public Page<Venta> getPedidoByUsuarioId(Long idUsuario, Pageable pageable) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario + "...");
        return ventaRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public Venta save(Venta venta) {
        log.info("Guardando pedido: " + venta + "...");
        checkPedido(venta);
        reservarPedido(venta);
        venta.setCreatedAt(LocalDateTime.now());
        venta.setUpdatedAt(LocalDateTime.now());
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#idPedido")
    public void delete(ObjectId idPedido) {
        log.info("Eliminando pedido: " + idPedido + "...");
        var pedidoDeleted = ventaRepository.findById(idPedido)
                .orElseThrow(() -> new VentaNotFound(idPedido.toHexString()));
        returnStockPedidos(pedidoDeleted);
        ventaRepository.deleteById(idPedido);
    }

    @Override
    @Transactional
    @CachePut(key = "#idPedido")
    public Venta update(ObjectId idPedido, Venta venta) {
        log.info("Actualizando pedido con id: " + idPedido + "...");
        ventaRepository.findById(idPedido).orElseThrow(() -> new VentaNotFound(idPedido.toHexString()));
        returnStockPedidos(venta);
        checkPedido(venta);
        var pedidoToSave = reservarPedido(venta);
        pedidoToSave.setUpdatedAt(LocalDateTime.now());
        return ventaRepository.save(pedidoToSave);
    }

    public void checkPedido(Venta venta) {
        log.info("Comprobando pedido");
        if (venta.getLineaVenta() == null || venta.getLineaVenta().isEmpty()) {
            throw new VentaNotItems(venta.get_id());
        }
        venta.getLineaVenta().forEach(lineaVenta -> {
            var funko = funkoRepository.findById(String.valueOf(lineaVenta.getIdFunko()))
                    .orElseThrow(() -> new FunkoNotFound(lineaVenta.getIdFunko()));
            if (funko.getStock() < lineaVenta.getCantidad() && lineaVenta.getCantidad() > 0) {
                throw new FunkoNotStock(lineaVenta.getIdFunko());
            }
            if (!funko.getPrecio().equals(lineaVenta.getPrecioUnitario())){
                throw new FunkoBadPrice(lineaVenta.getIdFunko());
            }
        });
    }

    private Venta reservarPedido(Venta venta) {
        log.info("Reservando pedido");
        if (venta.getLineaVenta() == null || venta.getLineaVenta().isEmpty()) {
            throw new VentaNotItems(venta.get_id());
        }
        venta.getLineaVenta().forEach(lineaVenta -> {
            var funko = funkoRepository.findById(String.valueOf(lineaVenta.getIdFunko())).get();
            funko.setStock(funko.getStock() - lineaVenta.getCantidad());
            funkoRepository.save(funko);
            lineaVenta.setTotal(lineaVenta.getCantidad() * lineaVenta.getPrecioUnitario());
        });

        var total = venta.getLineaVenta().stream()
                .map(lineaVenta -> lineaVenta.getCantidad() * lineaVenta.getPrecioUnitario())
                .reduce(0.0, Double::sum);

        var totalItems = venta.getLineaVenta().stream()
                .map(LineaVenta::getCantidad)
                .reduce(0, Integer::sum);

        venta.setTotal(total);
        venta.setTotalItems(totalItems);

        return venta;
    }

    Venta returnStockPedidos(Venta venta) {
        log.info("Retornando stock del pedido: {}", venta);
        if (venta.getLineaVenta() != null) {
            venta.getLineaVenta().forEach(lineaVenta -> {
                var funko = funkoRepository.findById(lineaVenta.getIdFunko()).get();
                funko.setStock(funko.getStock() + lineaVenta.getCantidad());
                funkoRepository.save(funko);
            });
        }
        return venta;
    }
}
