package org.example.springboot_funkos.rest.funkos.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.funkos.dto.FunkoDto;
import org.example.springboot_funkos.rest.funkos.mappers.FunkoMapper;
import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.rest.funkos.services.FunkoServiceImpl;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.example.springboot_funkos.utils.pagination.PageResponse;
import org.example.springboot_funkos.utils.pagination.PaginationLinksUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class FunkoControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    FunkoServiceImpl service;

    @MockBean
    PaginationLinksUtils paginationLinksUtils;

    @Autowired
    MockMvc mvc;

    FunkoMapper mapper = new FunkoMapper();
    Funko funkoTest = new Funko();
    Categoria categoriaTest = new Categoria();
    String myEndpoint = "/funkos";

    @Autowired
    private FunkoControllerTest(FunkoServiceImpl service, PaginationLinksUtils paginationLinksUtils) {
        this.service = service;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @BeforeEach
    void setUp() {
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("PELICULA");
        categoriaTest.setActivado(true);
        objectMapper.registerModule(new JavaTimeModule());

        funkoTest.setId(1L);
        funkoTest.setNombre("Darth Vader");
        funkoTest.setPrecio(10.99);
        funkoTest.setStock(20);
        funkoTest.setCategoria(categoriaTest);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllFunkos() throws Exception {
        Funko funko1 = new Funko(1L, "Mickey", 25.99, 10, null, null, null);
        Funko funko2 = new Funko(1L, "Iron Man", 30.99, 5, null, null, null);

        List<Funko> funkos = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Funko> page = new PageImpl<>(funkos, pageable, funkos.size());

        when(service.getAll(pageable)).thenReturn(page);

        var response = mvc.perform(get("/funkos")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        PageResponse<Funko> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(2, result.content().size());
        assertEquals("Mickey", result.content().get(0).getNombre());
        assertEquals("Iron Man", result.content().get(1).getNombre());

        verify(service, times(1)).getAll(pageable);
    }

    @Test
    void getById() throws Exception {
        when(service.getById(String.valueOf(1L))).thenReturn(funkoTest);

        MockHttpServletResponse response = mvc.perform(
                get(myEndpoint + "/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Funko res = objectMapper.readValue(response.getContentAsString(), Funko.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertEquals(res.getId(), funkoTest.getId()),
                () -> assertEquals(res.getNombre(), funkoTest.getNombre()),
                () -> assertEquals(res.getPrecio(), funkoTest.getPrecio()),
                () -> assertEquals(res.getCategoria(), funkoTest.getCategoria())
        );

        verify(service, times(1)).getById(String.valueOf(1L));
    }

    @Test
    void getByNombre() throws Exception {
        when(service.getByNombre("Darth Vader")).thenReturn(funkoTest);

        MockHttpServletResponse response = mvc.perform(
                        get(myEndpoint + "/nombre/Darth Vader")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Funko res = objectMapper.readValue(response.getContentAsString(), Funko.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertEquals(res.getId(), funkoTest.getId()),
                () -> assertEquals(res.getNombre(), funkoTest.getNombre()),
                () -> assertEquals(res.getStock(), funkoTest.getStock()),
                () -> assertEquals(res.getPrecio(), funkoTest.getPrecio()),
                () -> assertEquals(res.getCategoria(), funkoTest.getCategoria())
        );

        verify(service, times(1)).getByNombre("Darth Vader");
    }

    @Test
    void getByStock() throws Exception {
        when(service.getByStock(20)).thenReturn(Optional.ofNullable(funkoTest));

        MockHttpServletResponse response = mvc.perform(
                        get(myEndpoint + "/stock/20")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Funko res = objectMapper.readValue(response.getContentAsString(), Funko.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertEquals(res.getId(), funkoTest.getId()),
                () -> assertEquals(res.getNombre(), funkoTest.getNombre()),
                () -> assertEquals(res.getPrecio(), funkoTest.getPrecio()),
                () -> assertEquals(res.getCategoria(), funkoTest.getCategoria())
        );

        verify(service, times(1)).getByStock(20);
    }

    @Test
    void saveFunko() throws Exception {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("Mickey");
        funkoDto.setPrecio(20.55);
        funkoDto.setStock(15);
        funkoDto.setCategoria("Superheroe");

        Funko savedFunko = new Funko(1L, "Mickey", 25.99, 10, null, null, null);

        when(service.save(funkoDto)).thenReturn(savedFunko);

        var response = mvc.perform(post("/funkos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        Funko result = objectMapper.readValue(response.getContentAsString(), Funko.class);
        assertEquals("Mickey", result.getNombre());
        assertEquals(25.99, result.getPrecio());
        assertEquals(10, result.getStock());

        verify(service, times(1)).save(funkoDto);
    }

    @Test
    void updateFunko() throws Exception {
        // Crear un UUID para la categoría
        UUID categoriaId = UUID.randomUUID();
        Categoria categoria = new Categoria(categoriaId, "Disney", LocalDateTime.now(), LocalDateTime.now(), true);

        // Crear el DTO de Funko con la categoría
        Long funkoId = 1L;
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("Mickey");
        funkoDto.setPrecio(20.55);
        funkoDto.setStock(15);
        funkoDto.setCategoria("Disney"); // Asignar la categoría al DTO

        // Crear el Funko actualizado con la categoría
        Funko updatedFunko = new Funko(1L, "Mickey Updated", 28.99, 8, categoria, null, null);

        // Simular el comportamiento del servicio
        when(service.update(funkoId.toString(), funkoDto)).thenReturn(updatedFunko);

        // Realizar la solicitud PUT
        var response = mvc.perform(put("/funkos/" + funkoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // Convertir la respuesta en un objeto Funko
        Funko result = objectMapper.readValue(response.getContentAsString(), Funko.class);

        // Verificar que los valores de la respuesta sean correctos
        assertEquals("Mickey Updated", result.getNombre());
        assertEquals(28.99, result.getPrecio());
        assertEquals(8, result.getStock());
        assertEquals(categoriaId, result.getCategoria().getId()); // Verificar que la categoría sea correcta

        // Verificar que el servicio se haya llamado correctamente
        verify(service, times(1)).update(funkoId.toString(), funkoDto);
    }

    @Test
    void delete() throws Exception {
        when(service.delete(String.valueOf(1L))).thenReturn(funkoTest);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.delete(myEndpoint + "/1")
                       .accept(MediaType.APPLICATION_JSON))
               .andReturn().getResponse();

        assertEquals(response.getStatus(), HttpStatus.OK.value());

        verify(service, times(1)).delete(String.valueOf(1L));
    }

    @Test
    void nombreIsBlank() throws Exception {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("");
        funkoDto.setPrecio(10.00);
        funkoDto.setCategoria("CategoriaTest");

        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre no puede estar vacio"))
                .andReturn();
    }

    @Test
    void precioMenorCero() throws Exception {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("FunkoTest");
        funkoDto.setPrecio(-1.00);
        funkoDto.setCategoria("CategoriaTest");

        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.precio").value("debe ser mayor que o igual a 0"))
                .andReturn();
    }

    @Test
    void precioMayorCincuenta() throws Exception {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("FunkoTest");
        funkoDto.setPrecio(51.00);
        funkoDto.setCategoria("CategoriaTest");

        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.precio").value("debe ser menor que o igual a 50"))
                .andReturn();
    }

    @Test
    void precioNull() throws Exception {
        FunkoDto funkoDtoConPrecioNulo = new FunkoDto();
        funkoDtoConPrecioNulo.setNombre("FunkoTest");
        funkoDtoConPrecioNulo.setPrecio(null);
        funkoDtoConPrecioNulo.setCategoria("CategoriaTest");

        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDtoConPrecioNulo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.precio").value("El precio no puede ser un campo nulo"))
                .andReturn();
    }

    @Test
    void categoriaBlank() throws Exception {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("FunkoTest");
        funkoDto.setPrecio(10.00);
        funkoDto.setCategoria("");

        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funkoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.categoria").value("La categoria no puede estar vacia"))
                .andReturn();
    }

    @Test
    void testValidationExceptionHandler() throws Exception {
        mvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre no puede estar vacio"))
                .andReturn();
    }
}