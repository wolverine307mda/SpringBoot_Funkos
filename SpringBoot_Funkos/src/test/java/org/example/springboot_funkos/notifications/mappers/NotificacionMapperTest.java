package org.example.springboot_funkos.notifications.mappers;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.notifications.dto.NotificacionDto;
import org.example.springboot_funkos.notifications.models.Notificacion;
import org.example.springboot_funkos.rest.categoria.model.Categoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionMapperTest {

    private final Categoria categoria = new Categoria(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"), "DISNEY", LocalDateTime.now(), LocalDateTime.now(), true);
    private final Funko funko = new Funko(
            1L, // ID
            "Darth Vader",
            15.99,
            100,
            categoria,
            LocalDateTime.now(),
            LocalDateTime.now()
    );
    private final NotificacionMapper mapper = new NotificacionMapper();

    @Test
    void toNotificationDto() {
        NotificacionDto res = mapper.toNotificationDto(funko);

        assertAll(
                () -> assertEquals(funko.getId(), res.id()),
                () -> assertEquals(funko.getNombre(), res.nombre()),
                () -> assertEquals(funko.getCategoria().getNombre(), res.categoria()),
                () -> assertEquals(funko.getPrecio(), res.precio()),
                () -> assertEquals(funko.getCreatedAt().toString(), res.createdAt()),
                () -> assertEquals(funko.getUpdatedAt().toString(), res.updatedAt())
        );
    }

    @Test
    void toNotificationDtoWithValidAndInvalidValues() {
        Funko funkoWithInvalidValues = new Funko(
                2L,
                "Iron Man",
                -5.99, // Invalid price
                0, // Stock
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        NotificacionDto res = mapper.toNotificationDto(funkoWithInvalidValues);

        assertAll(
                () -> assertEquals(funkoWithInvalidValues.getId(), res.id()),
                () -> assertEquals(funkoWithInvalidValues.getNombre(), res.nombre()),
                () -> assertEquals(funkoWithInvalidValues.getCategoria().getNombre(), res.categoria()),
                () -> assertEquals(funkoWithInvalidValues.getPrecio(), res.precio()),
                () -> assertEquals(funkoWithInvalidValues.getCreatedAt().toString(), res.createdAt()),
                () -> assertEquals(funkoWithInvalidValues.getUpdatedAt().toString(), res.updatedAt())
        );
    }

    @Test
    void toNotificationWithNotificacion() {
        Notificacion<NotificacionDto> notificacion = new Notificacion<>(
                "Funko",
                Notificacion.Tipo.CREATE,
                mapper.toNotificationDto(funko),
                LocalDateTime.now().toString()
        );

        assertAll(
                () -> assertEquals("Funko", notificacion.entity()),
                () -> assertEquals(Notificacion.Tipo.CREATE, notificacion.type()),
                () -> assertEquals(funko.getId(), notificacion.data().id()),
                () -> assertEquals(funko.getNombre(), notificacion.data().nombre()),
                () -> assertEquals(funko.getCategoria().getNombre(), notificacion.data().categoria()),
                () -> assertEquals(funko.getPrecio(), notificacion.data().precio()),
                () -> assertEquals(funko.getCreatedAt().toString(), notificacion.data().createdAt()),
                () -> assertEquals(funko.getUpdatedAt().toString(), notificacion.data().updatedAt())
        );
    }
}
