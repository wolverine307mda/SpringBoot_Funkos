package org.example.springboot_funkos.rest.venta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VentaNotItems extends VentaException {
    public VentaNotItems(String id) {
        super("Pedido con id " + id + " no contiene items");
    }
}