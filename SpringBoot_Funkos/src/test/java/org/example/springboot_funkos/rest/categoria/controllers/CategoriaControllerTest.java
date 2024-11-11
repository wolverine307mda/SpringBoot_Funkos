package org.example.springboot_funkos.rest.categoria.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springboot_funkos.rest.categoria.dto.CategoriaDto;
import org.example.springboot_funkos.rest.categoria.mappers.CategoriaMapper;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.example.springboot_funkos.rest.categoria.services.CategoriaServiceImpl;
import org.example.springboot_funkos.utils.pagination.PageResponse;
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


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    CategoriaServiceImpl service;

    @Autowired
    MockMvc mvc;

    CategoriaMapper mapper = new CategoriaMapper();
    Categoria categoriaTest = new Categoria();
    String myEndpoint = "/categorias";

    @Autowired
    private CategoriaControllerTest(CategoriaServiceImpl service) {
        this.service = service;
    }

    @BeforeEach
    void setUp() {
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("DISNEY");
        categoriaTest.setCreatedAt(LocalDateTime.now());
        categoriaTest.setUpdatedAt(LocalDateTime.now());
        categoriaTest.setActivado(true);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllCategorias() throws Exception {
        // Configuración de objetos de prueba
        Categoria categoria1 = new Categoria();
        categoria1.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        categoria1.setNombre("DISNEY");
        categoria1.setActivado(true);

        Categoria categoria2 = new Categoria();
        categoria2.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        categoria2.setNombre("MARVEL");
        categoria2.setActivado(true);

        // Simulación de datos paginados
        List<Categoria> list = List.of(categoria1, categoria2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Categoria> page = new PageImpl<>(list, pageable, list.size());

        // Configuración del mock para el servicio
        when(service.getAll(pageable)).thenReturn(page);

        // Realizar la petición al endpoint
        MockHttpServletResponse response = mvc.perform(
                        get(myEndpoint)
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // Leer y verificar la respuesta
        PageResponse<CategoriaDto> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findAllCategorias",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size()),
                () -> assertEquals("DISNEY", res.content().get(0).getNombre()),
                () -> assertEquals("MARVEL", res.content().get(1).getNombre())
        );

        // Verificar interacción con el servicio
        verify(service, times(1)).getAll(pageable);
    }

    @Test
    void getById() throws Exception {
        when(service.getById("12d45756-3895-49b2-90d3-c4a12d5ee081")).thenReturn(categoriaTest);

        MockHttpServletResponse response = mvc.perform(
                        get(myEndpoint + "/12d45756-3895-49b2-90d3-c4a12d5ee081")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertEquals(res.getId(), categoriaTest.getId()),
                () -> assertEquals(res.getNombre(), categoriaTest.getNombre()),
                () -> assertEquals(res.getActivado(), categoriaTest.getActivado())
        );

        verify(service, times(1)).getById("12d45756-3895-49b2-90d3-c4a12d5ee081");
    }

    @Test
    void save() throws Exception {
        CategoriaDto nuevoCategoria = new CategoriaDto();
        nuevoCategoria.setNombre("DISNEY");
        nuevoCategoria.setActivado(true);

        when(service.save(nuevoCategoria)).thenReturn(mapper.toCategoria(nuevoCategoria));

        MockHttpServletResponse response = mvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevoCategoria)))
                .andReturn().getResponse();

        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.CREATED.value()),
                () -> assertEquals(res.getId(), mapper.toCategoria(nuevoCategoria).getId()),
                () -> assertEquals(res.getNombre(), mapper.toCategoria(nuevoCategoria).getNombre()),
                () -> assertEquals(res.getActivado(), nuevoCategoria.getActivado())
        );

        verify(service, times(1)).save(nuevoCategoria);
    }

    @Test
    void update() throws Exception {
        CategoriaDto updatedCategoria = new CategoriaDto();
        updatedCategoria.setNombre("SUPERHEROES");
        updatedCategoria.setActivado(true);

        Categoria expectedCategoria = new Categoria();
        expectedCategoria.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        expectedCategoria.setNombre("SUPERHEROES");
        expectedCategoria.setActivado(true);

        when(service.update("12d45756-3895-49b2-90d3-c4a12d5ee081", updatedCategoria)).thenReturn(expectedCategoria);

        MockHttpServletResponse response = mvc.perform(
                        patch(myEndpoint + "/12d45756-3895-49b2-90d3-c4a12d5ee081")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedCategoria)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);
        assertAll(
                () -> assertEquals(res.getId(), expectedCategoria.getId()),
                () -> assertEquals(res.getNombre(), expectedCategoria.getNombre()),
                () -> assertEquals(res.getActivado(), expectedCategoria.getActivado())
        );

        verify(service, times(1)).update("12d45756-3895-49b2-90d3-c4a12d5ee081", updatedCategoria);
    }


    @Test
    void delete() throws Exception {
        CategoriaDto deletedCategoria = new CategoriaDto();
        deletedCategoria.setNombre("SUPERHEROES");
        deletedCategoria.setActivado(true);

        when(service.update("12d45756-3895-49b2-90d3-c4a12d5ee081", deletedCategoria)).thenReturn(mapper.toCategoria(deletedCategoria));

        MockHttpServletResponse response = mvc.perform(
                        patch(myEndpoint + "/12d45756-3895-49b2-90d3-c4a12d5ee081")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(deletedCategoria)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertEquals(res.getId(), mapper.toCategoria(deletedCategoria).getId()),
                () -> assertEquals(res.getNombre(), mapper.toCategoria(deletedCategoria).getNombre()),
                () -> assertEquals(res.getActivado(), deletedCategoria.getActivado())
        );

        verify(service, times(1)).update("12d45756-3895-49b2-90d3-c4a12d5ee081", deletedCategoria);
    }


    @Test
    void nombreIsBlank() throws Exception {
        CategoriaDto nuevoCategoria = new CategoriaDto();
        nuevoCategoria.setNombre("");
        nuevoCategoria.setActivado(true);

        MockHttpServletResponse response = mvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevoCategoria)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String responseContent = response.getContentAsString();

        assertTrue(responseContent.contains("El nombre no puede estar vacio"));
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

    @Test
    void updatePartialCategoria() throws Exception {
        CategoriaDto partialUpdate = new CategoriaDto();
        partialUpdate.setNombre("NUEVO NOMBRE");

        Categoria expectedCategoria = new Categoria();
        expectedCategoria.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        expectedCategoria.setNombre("NUEVO NOMBRE");
        expectedCategoria.setActivado(true);

        when(service.update("12d45756-3895-49b2-90d3-c4a12d5ee081", partialUpdate)).thenReturn(expectedCategoria);

        MockHttpServletResponse response = mvc.perform(
                        patch(myEndpoint + "/12d45756-3895-49b2-90d3-c4a12d5ee081")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(partialUpdate)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(res.getNombre(), partialUpdate.getNombre()),
                () -> assertEquals(res.getActivado(), expectedCategoria.getActivado())
        );

        verify(service, times(1)).update("12d45756-3895-49b2-90d3-c4a12d5ee081", partialUpdate);
    }

    @Test
    void activateCategoria() throws Exception {
        // Configuración de la categoría esperada con el estado "activado" como true
        CategoriaDto updatedCategoria = new CategoriaDto();
        updatedCategoria.setActivado(true);

        Categoria expectedCategoria = new Categoria();
        expectedCategoria.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        expectedCategoria.setNombre("DISNEY");
        expectedCategoria.setActivado(true);

        // Simular la actualización de estado en el servicio
        when(service.update("12d45756-3895-49b2-90d3-c4a12d5ee081", updatedCategoria)).thenReturn(expectedCategoria);

        // Realizar la petición PATCH para actualizar el estado de "activado" a true
        MockHttpServletResponse response = mvc.perform(
                        patch(myEndpoint + "/12d45756-3895-49b2-90d3-c4a12d5ee081")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedCategoria))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Validar el estado HTTP y el contenido de la respuesta
        Categoria res = objectMapper.readValue(response.getContentAsString(), Categoria.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(res.getActivado()),
                () -> assertEquals(res.getId(), expectedCategoria.getId())
        );

        // Verificar que el servicio fue llamado correctamente
        verify(service, times(1)).update("12d45756-3895-49b2-90d3-c4a12d5ee081", updatedCategoria);
    }
}