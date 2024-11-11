package org.example.springboot_funkos.rest.venta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VentaNotFound extends VentaException {
    public VentaNotFound(String id) {
        super("Pedido con id " + id + " no encontrado");
    }
}