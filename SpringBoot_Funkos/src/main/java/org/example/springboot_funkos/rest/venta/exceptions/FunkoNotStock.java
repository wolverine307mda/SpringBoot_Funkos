package org.example.springboot_funkos.rest.venta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoNotStock extends VentaException {
    public FunkoNotStock(Long id) {
        super("Cantidad no válida o Funko con id " + id + " no tiene stock suficiente");
    }
}