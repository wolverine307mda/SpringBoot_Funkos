package org.example.springboot_funkos.storage.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IStorageService storageService;

    @Test
    public void testServeFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Contenido del archivo".getBytes());

        when(storageService.loadAsResource(any())).thenReturn(mockFile.getResource());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/funkos/files/" + mockFile.getName()))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals("Contenido del archivo", response.getContentAsString())
        );
    }
 */
}