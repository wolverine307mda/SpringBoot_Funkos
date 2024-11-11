package org.example.springboot_funkos.rest.storage.controller;

import org.example.springboot_funkos.rest.storage.service.StorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StorageServiceImpl storageService;

    @Test
    void testServeFile() throws Exception {

    }
}