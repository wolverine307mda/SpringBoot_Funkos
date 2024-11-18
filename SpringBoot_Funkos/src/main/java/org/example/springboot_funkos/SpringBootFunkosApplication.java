package org.example.springboot_funkos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@Slf4j
public class SpringBootFunkosApplication implements CommandLineRunner {
    @Value("${spring.profiles.active}")
    private String perfil;
    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFunkosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("🟢 Servidor escuchando en puerto: {} y perfil: {} 🚀", port, perfil);
    }
}
