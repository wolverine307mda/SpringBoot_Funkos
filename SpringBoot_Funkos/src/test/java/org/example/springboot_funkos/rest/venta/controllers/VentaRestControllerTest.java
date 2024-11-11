package org.example.springboot_funkos.rest.venta.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springboot_funkos.rest.venta.exceptions.*;
import org.example.springboot_funkos.rest.venta.models.Cliente;
import org.example.springboot_funkos.rest.venta.models.Direccion;
import org.example.springboot_funkos.rest.venta.models.LineaVenta;
import org.example.springboot_funkos.rest.venta.models.Venta;
import org.example.springboot_funkos.rest.venta.services.VentaService;
import org.example.springboot_funkos.utils.pagination.PageResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
// Solo para admin por ahora
class VentaRestControllerTest {
    private final String myEndpoint = "/v1/ventas";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Venta venta1 = Venta.builder()
            .id(new ObjectId("5f9f1a3b9d6b6d2e3c1d6f1a"))
            .idUsuario(1L)
            .cliente(
                    new Cliente("Mario", "wolve@dev.com", "1234567890",
                            new Direccion("Calle", "1", "Ciudad", "Provincia", "Pais", "12345")
                    )
            )
            .lineaVenta(List.of(LineaVenta.builder()
                    .idFunko(1L)
                    .cantidad(2)
                    .precioUnitario(10.0)
                    .build()))
            .build();
    @Autowired
    MockMvc mockMvc; // Cliente MVC
    @MockBean
    private VentaService ventaService;

    @Autowired
    public VentaRestControllerTest(VentaService ventaService) {
        this.ventaService = ventaService;
        mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    @Test
    void getAllVentas() throws Exception {
        var ventasList = List.of(venta1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(ventasList);

        // Arrange
        when(ventaService.getAll(pageable)).thenReturn(page);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Venta> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(ventaService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getVentaById() throws Exception {
        // Arrange
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(ventaService.getById(any(ObjectId.class))).thenReturn(venta1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Venta res = mapper.readValue(response.getContentAsString(), Venta.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(venta1, res)
        );

        // Verify
        verify(ventaService, times(1)).getById(any(ObjectId.class));
    }

    @Test
    void getPedidoByIdNoFound() throws Exception {
        // Arrange
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(ventaService.getById(any(ObjectId.class)))
                .thenThrow(new VentaNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(ventaService, times(1)).getById(any(ObjectId.class));
    }

    @Test
    void getPedidosByUsuario() throws Exception {
        // Arrange
        var myLocalEndpoint = myEndpoint + "/usuario/1";
        var pedidosList = List.of(venta1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(pedidosList);

        // Arrange
        when(ventaService.getPedidoByUsuarioId(anyLong(), any(Pageable.class))).thenReturn(page);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Venta> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(ventaService, times(1)).getPedidoByUsuarioId(anyLong(), any(Pageable.class));
    }

    @Test
    void createPedido() throws Exception {
        // Arrange
        when(ventaService.save(any(Venta.class))).thenReturn(venta1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        Venta res = mapper.readValue(response.getContentAsString(), Venta.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(venta1, res)
        );

        // Verify
        verify(ventaService, times(1)).save(any(Venta.class));
    }

    @Test
    void createPedidoNoItemsBadRequest() throws Exception {
        // Arrange
        when(ventaService.save(any(Venta.class))).thenThrow(new VentaNotItems("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(ventaService).save(any(Venta.class));
    }

    @Test
    void createPedidoProductoBadPriceBadRequest() throws Exception {
        // Arrange
        when(ventaService.save(any(Venta.class))).thenThrow(new FunkoBadPrice(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(ventaService).save(any(Venta.class));
    }

    @Test
    void getPedidosProductoNotFoundBadRequest() throws Exception {
        // Arrange
        when(ventaService.save(any(Venta.class))).thenThrow(new FunkoNotFound(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(ventaService).save(any(Venta.class));
    }

    @Test
    void getPedidosProductoNotStockBadRequest() throws Exception {
        // Arrange
        when(ventaService.save(any(Venta.class))).thenThrow(new FunkoNotStock(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        // Verify
        verify(ventaService).save(any(Venta.class));
    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(ventaService.update(any(ObjectId.class), any(Venta.class))).thenReturn(venta1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        Venta res = mapper.readValue(response.getContentAsString(), Venta.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(venta1, res)
        );

        // Verify
        verify(ventaService, times(1)).update(any(ObjectId.class), any(Venta.class));
    }

    @Test
    void updatePedidoNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        when(ventaService.update(any(ObjectId.class), any(Venta.class)))
                .thenThrow(new VentaNotFound("5f9f1a3b9d6b6d2e3c1d6f1a"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(venta1)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(ventaService, times(1)).update(any(ObjectId.class), any(Venta.class));
    }

    // Habría que testear casi lo mismo en el save con el update

    @Test
    void deletePedido() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        doNothing().when(ventaService).delete(any(ObjectId.class));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        // Verify
        verify(ventaService, times(1)).delete(any(ObjectId.class));
    }

    @Test
    void deletePedidoNoFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        // Arrange
        doThrow(new VentaNotFound("5f9f1a3b9d6b6d2e3c1d6f1a")).when(ventaService).delete(any(ObjectId.class));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(ventaService, times(1)).delete(any(ObjectId.class));
    }
}