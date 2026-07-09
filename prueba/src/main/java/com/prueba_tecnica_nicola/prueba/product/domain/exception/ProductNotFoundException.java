package com.prueba_tecnica_nicola.prueba.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Producto con id " + id + " no encontrado");
    }
}