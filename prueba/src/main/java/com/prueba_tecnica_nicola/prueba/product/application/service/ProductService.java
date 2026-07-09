package com.prueba_tecnica_nicola.prueba.product.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prueba_tecnica_nicola.prueba.product.application.port.in.ProductServicePort;
import com.prueba_tecnica_nicola.prueba.product.application.port.out.ProductRepositoryPort;
import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.InvalidProductException;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.ProductNotFoundException;

@Service
public class ProductService implements ProductServicePort {

    private final ProductRepositoryPort repositoryPort;

    public ProductService(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Product createProduct(Product product) {
        validateProductData(product);
        Product toCreate = new Product(
            null,
            product.name(),
            product.description(),
            product.price(),
            product.stock(),
            null,
            null
        );
        return repositoryPort.save(toCreate);
    }

    @Override
    public List<Product> getAllProducts(int page, int size) {
        validatePage(page, size);
        return repositoryPort.findAll(page, size);
    }

    @Override
    public Product getProductById(Long id) {
        return repositoryPort.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        validateProductData(product);
        Product existing = repositoryPort.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        Product updated = new Product(
            existing.id(),
            product.name(),
            product.description(),
            product.price(),
            product.stock(),
            existing.createdAt(),
            null
        );
        return repositoryPort.save(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        if (repositoryPort.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        repositoryPort.deleteById(id);
    }

    @Override
    public long countProducts() {
        return repositoryPort.count();
    }

    private void validatePage(int page, int size) {
        if (page < 0) {
            throw new InvalidProductException("page debe ser >= 0");
        }
        if (size <= 0) {
            throw new InvalidProductException("size debe ser > 0");
        }
    }

    private void validateProductData(Product product) {
        if (product == null) {
            throw new InvalidProductException("El producto es obligatorio");
        }
        if (product.name() == null || product.name().isBlank()) {
            throw new InvalidProductException("name es obligatorio");
        }
        if (product.name().length() > 150) {
            throw new InvalidProductException("name debe tener maximo 150 caracteres");
        }
        if (product.price() == null || product.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("price debe ser mayor que 0");
        }
        if (product.stock() == null || product.stock() < 0) {
            throw new InvalidProductException("stock debe ser mayor o igual a 0");
        }
    }
}