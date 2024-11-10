package org.example.springboot_funkos;

import org.example.springboot_funkos.rest.funkos.services.FunkoServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SpringBootFunkosApplication implements CommandLineRunner {

    @Autowired
    private FunkoServiceImpl funkoService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFunkosApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }
}
