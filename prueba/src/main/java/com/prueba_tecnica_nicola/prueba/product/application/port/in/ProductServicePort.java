package com.prueba_tecnica_nicola.prueba.product.application.port.in;

import java.util.List;

import com.prueba_tecnica_nicola.prueba.product.domain.Product;

public interface ProductServicePort {
    Product createProduct(Product product);
    List<Product> getAllProducts(int page, int size);
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    long countProducts();
}