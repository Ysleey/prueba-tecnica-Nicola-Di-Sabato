package com.prueba_tecnica_nicola.prueba.product.application.port.out;

import java.util.List;
import java.util.Optional;

import com.prueba_tecnica_nicola.prueba.product.domain.Product;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll(int page, int size);

    long count();

    void deleteById(Long id);
}