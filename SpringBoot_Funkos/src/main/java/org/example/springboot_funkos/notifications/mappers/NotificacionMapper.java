package org.example.springboot_funkos.notifications.mappers;

import org.example.springboot_funkos.rest.funkos.model.Funko;
import org.example.springboot_funkos.notifications.dto.NotificacionDto;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {
    public NotificacionDto toNotificationDto(Funko funko) {
        return new NotificacionDto(
                funko.getId(),
                funko.getNombre(),
                funko.getCategoria().getNombre(),  // Cambiado para obtener el nombre de la categoría
                funko.getPrecio(),
                funko.getCreatedAt().toString(),
                funko.getUpdatedAt().toString()
        );
    }
}
