package org.example.springboot_funkos.notifications.dto;

public record NotificacionDto(
        Long id,
        String nombre,
        String categoria,
        Double precio,
        String createdAt,
        String updatedAt
) {
}