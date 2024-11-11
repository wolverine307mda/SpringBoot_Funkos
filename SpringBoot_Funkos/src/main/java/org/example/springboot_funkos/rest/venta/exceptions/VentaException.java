package org.example.springboot_funkos.rest.venta.exceptions;

public abstract class VentaException extends RuntimeException {
    public VentaException(String message) {
        super(message);
    }
}