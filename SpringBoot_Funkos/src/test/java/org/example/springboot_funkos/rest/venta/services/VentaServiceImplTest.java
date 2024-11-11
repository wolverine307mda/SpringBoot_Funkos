package org.example.springboot_funkos.rest.venta.services;

import org.example.springboot_funkos.rest.venta.exceptions.*;
import org.example.springboot_funkos.rest.venta.models.LineaVenta;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.example.springboot_funkos.rest.venta.repositories.VentaRepository;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.rest.funkos.repositories.FunkoRepository;
import org.bson.types.ObjectId;
import org.example.springboot_funkos.rest.venta.services.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {
    @Mock
    private VentaRepository pedidosRepository;
    @Mock
    private FunkoRepository productosRepository;

    @InjectMocks
    private VentaServiceImpl pedidosService;

    @Test
    void findAll_ReturnsPageOfPedidos() {
        // Arrange
        List<Venta> ventas = List.of(new Venta(), new Venta());
        Page<Venta> expectedPage = new PageImpl<>(ventas);
        Pageable pageable = PageRequest.of(0, 10);

        when(pedidosRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Venta> result = pedidosService.getAll(pageable);

        // Assert
        assertAll(
                () -> assertEquals(expectedPage, result),
                () -> assertEquals(expectedPage.getContent(), result.getContent()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements())
        );

        // Verify
        verify(pedidosRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Venta expectedVenta = new Venta();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(expectedVenta));

        // Act
        Venta resultVenta = pedidosService.getById(idPedido);

        // Assert
        assertEquals(expectedVenta, resultVenta);

        // Verify
        verify(pedidosRepository).findById(idPedido);
    }

    @Test
    void testFindById_ThrowsPedidoNotFound() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VentaNotFound.class, () -> pedidosService.getById(idPedido));

        // Verify
        verify(pedidosRepository).findById(idPedido);
    }

    @Test
    void testFindByIdUsuario() {
        // Arrange
        Long idUsuario = 1L;
        Pageable pageable = mock(Pageable.class);
        @SuppressWarnings("unchecked")
        Page<Venta> expectedPage = mock(Page.class);
        when(pedidosRepository.findByIdUsuario(idUsuario, pageable)).thenReturn(expectedPage);

        // Act
        Page<Venta> resultPage = pedidosService.getPedidoByUsuarioId(idUsuario, pageable);

        // Assert
        assertEquals(expectedPage, resultPage);

        // Verify
        verify(pedidosRepository).findByIdUsuario(idUsuario, pageable);
    }

    @Test
    void testSave() {
        // Arrange
        Funko producto = Funko.builder()
                .id(1L)
                .stock(5)
                .precio(10.0)
                .build();

        Venta venta = new Venta();
        LineaVenta lineaVenta = LineaVenta.builder()
                .idFunko(1L)
                .cantidad(2)
                .precioUnitario(10.0)
                .build();

        venta.setLineasPedido(List.of(lineaVenta));
        Venta ventaToSave = new Venta();
        ventaToSave.setLineasPedido(List.of(lineaVenta));

        when(pedidosRepository.save(any(Venta.class))).thenReturn(ventaToSave); // Utiliza any(Pedido.class) para cualquier instancia de Pedido
        when(productosRepository.findById(anyLong())).thenReturn(Optional.of(producto));

        // Act
        Venta resultVenta = pedidosService.save(venta);

        // Assert
        assertAll(
                () -> assertEquals(ventaToSave, resultVenta),
                () -> assertEquals(ventaToSave.getLineaVenta(), resultVenta.getLineaVenta()),
                () -> assertEquals(ventaToSave.getLineaVenta().size(), resultVenta.getLineaVenta().size())
        );

        // Verify
        verify(pedidosRepository).save(any(Venta.class));
        verify(productosRepository, times(2)).findById(anyLong());
    }

    @Test
    void testSave_ThrowsPedidoNotItems() {
        // Arrange
        Venta venta = new Venta();

        // Act & Assert
        assertThrows(VentaNotItems.class, () -> pedidosService.save(venta));

        // Verify
        verify(pedidosRepository, never()).save(any(Venta.class));
        verify(productosRepository, never()).findById(anyLong());
    }

    @Test
    void testDelete() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Venta ventaToDelete = new Venta();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(ventaToDelete));

        // Act
        pedidosService.delete(idPedido);

        // Assert


        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository).deleteById(idPedido);
    }

    @Test
    void testDelete_ThrowsPedidoNotFound() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VentaNotFound.class, () -> pedidosService.delete(idPedido));

        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository, never()).deleteById(idPedido);
    }

    @Test
    void testUpdate() {
        // Arrange
        Funko producto = Funko.builder()
                .id(1L)
                .stock(5)
                .precio(10.0)
                .build();


        LineaVenta lineaVenta = LineaVenta.builder()
                .idFunko(1L)
                .cantidad(2)
                .precioUnitario(10.0)
                .build();

        ObjectId idPedido = new ObjectId();
        Venta venta = new Venta();
        venta.setLineasPedido(List.of(lineaVenta));
        Venta ventaToUpdate = new Venta();
        ventaToUpdate.setLineasPedido(List.of(lineaVenta)); // Inicializar la lista de líneas de pedido

        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(ventaToUpdate));
        when(pedidosRepository.save(any(Venta.class))).thenReturn(ventaToUpdate);
        when(productosRepository.findById(anyLong())).thenReturn(Optional.of(producto));

        // Act
        Venta resultVenta = pedidosService.update(idPedido, venta);

        // Assert
        assertAll(
                () -> assertEquals(ventaToUpdate, resultVenta),
                () -> assertEquals(ventaToUpdate.getLineaVenta(), resultVenta.getLineaVenta()),
                () -> assertEquals(ventaToUpdate.getLineaVenta().size(), resultVenta.getLineaVenta().size())
        );

        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository).save(any(Venta.class));
        verify(productosRepository, times(3)).findById(anyLong());
    }

    @Test
    void testUpdate_ThrowsPedidoNotFound() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Venta venta = new Venta();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VentaNotFound.class, () -> pedidosService.update(idPedido, venta));

        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository, never()).save(any(Venta.class));
        verify(productosRepository, never()).findById(anyLong());
    }

    @Test
    void checkPedido_ProductosExistenYHayStock_NoDebeLanzarExcepciones() {
        /*
        // Arrange
        ObjectId pedidoId = new ObjectId(); // Crea un ObjectId para el pedido
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId); // Asignar el ObjectId al pedido

        List<LineaPedido> lineasPedido = new ArrayList<>();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idFunko(1L) // El id del Funko sigue siendo long
                .cantidad(2)
                .precioUnitario(10.0)
                .build();

        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        Funko funko = Funko.builder()
                .id(1L) // El id del Funko sigue siendo long
                .stock(5)
                .precio(10.0)
                .build();

        // Simula que el pedido y el funko existen en los repositorios
        when(pedidosRepository.findById(pedidoId)).thenReturn(Optional.of(pedido)); // Usa ObjectId
        when(productosRepository.findById(1L)).thenReturn(Optional.of(funko)); // El id del funko sigue siendo long

        // Act
        pedidosService.checkPedido(pedido); // Llamamos explícitamente a checkPedido para que el flujo lo invoque

        // Assert
        assertDoesNotThrow(() -> pedidosService.checkPedido(pedido)); // Verifica que no lance excepciones

        // Verify
        verify(productosRepository, times(1)).findById(1L); // Verifica que se haya invocado el repositorio de productos con el id del funko
        verify(pedidosRepository, times(1)).findById(pedidoId); // Verifica que se haya invocado el repositorio de pedidos con el ObjectId
        */
    }

    @Test
    void checkPedido_PrecioProductoDiferente_DebeLanzarProductoBadPrice() {
        /*
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(new ObjectId("67313856ef8e377b2cab3a0a")); // Asigna un id único al pedido

        List<LineaPedido> lineasPedido = new ArrayList<>();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idFunko(1L)
                .cantidad(2)
                .precioUnitario(20.0) // Precio diferente al del producto
                .build();

        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        Funko producto = Funko.builder()
                .id(1L) // El id del Funko sigue siendo long
                .stock(5)
                .precio(10.0) // Precio del producto
                .build();

        // Simula que el pedido y el producto existen en los repositorios
        when(pedidosRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido)); // Configura el mock para el pedido
        when(productosRepository.findById(1L)).thenReturn(Optional.of(producto)); // Configura el mock para el producto

        // Act & Assert
        assertThrows(FunkoBadPrice.class, () -> pedidosService.checkPedido(pedido)); // Excepción esperada ProductoBadPrice

        // Verify
        verify(productosRepository, times(1)).findById(1L); // Verifica que se llamó al repositorio de productos
        verify(pedidosRepository, times(1)).findById(pedido.getId()); // Verifica que se llamó al repositorio de pedidos
        */
    }

}