plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm")
    id("jacoco")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencias de Spring Web para HTML Apps y Rest
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA para SQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Data MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Validación
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    implementation("org.glassfish:javax.el:3.0.0")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")

    // WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Bases de datos
    implementation("com.h2database:h2") // Para desarrollo
    implementation("org.postgresql:postgresql") // Para producción

    // Manejo de fechas con Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Negociación de contenido con XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

    // Manejo de JWT tokens
    implementation("com.auth0:java-jwt:4.4.0")

    // Swagger (Documentación)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // Bootstrap para vistas
    implementation("org.webjars:bootstrap:4.6.2")

    // Dependencias para tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.18.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}
