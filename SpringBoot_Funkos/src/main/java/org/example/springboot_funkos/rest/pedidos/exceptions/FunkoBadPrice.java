package org.example.springboot_funkos.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoBadPrice extends PedidoException {
    public FunkoBadPrice(Long id) {
        super("Funko con id " + id + " no tiene un precio válido o no coincide con su precio actual");
    }
}