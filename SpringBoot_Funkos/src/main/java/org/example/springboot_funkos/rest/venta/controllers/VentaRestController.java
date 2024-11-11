package org.example.springboot_funkos.rest.venta.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.example.springboot_funkos.rest.venta.services.VentaService;
import org.example.springboot_funkos.utils.pagination.PageResponse;
import org.example.springboot_funkos.utils.pagination.PaginationLinksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("${api.version}/ventas")
@Slf4j
public class VentaRestController {
    private final VentaService ventaService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public VentaRestController(VentaService ventaService, PaginationLinksUtils paginationLinksUtils) {
        this.ventaService = ventaService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<Venta>> getAllPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Obteniendo todos los pedidos");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Venta> pageResult = ventaService.getAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getPedido(@PathVariable("id") ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return ResponseEntity.ok(ventaService.getById(idPedido));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<PageResponse<Venta>> getPedidosByUsuario(
            @PathVariable("id") Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(ventaService.getPedidoByUsuarioId(idUsuario, pageable), sortBy, direction));
    }

    @PostMapping()
    public ResponseEntity<Venta> createPedido(@Valid @RequestBody Venta venta) {
        log.info("Creando pedido: " + venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.save(venta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updatePedido(@PathVariable("id") ObjectId idPedido, @Valid @RequestBody Venta venta) {
        log.info("Actualizando pedido con id: " + idPedido);
        return ResponseEntity.ok(ventaService.update(idPedido, venta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venta> deletePedido(@PathVariable("id") ObjectId idPedido) {
        log.info("Borrando pedido con id: " + idPedido);
        ventaService.delete(idPedido);
        return ResponseEntity.noContent().build();
    }
}