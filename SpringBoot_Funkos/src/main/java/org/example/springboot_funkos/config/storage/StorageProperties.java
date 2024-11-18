package org.example.springboot_funkos.config.storage;


import lombok.extern.slf4j.Slf4j;
import org.example.springboot_funkos.rest.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StorageProperties {

    @Bean
    public CommandLineRunner init(IStorageService storageService, @Value("${upload.delete}") String deleteAll) {
        return args -> {

            if (deleteAll.equals("true")) {
                log.info("Borrando ficheros de almacenamiento...");
                storageService.deleteAll();
            }

            storageService.init(); // inicializamos
        };
    }

    private String location = "imagenes";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}