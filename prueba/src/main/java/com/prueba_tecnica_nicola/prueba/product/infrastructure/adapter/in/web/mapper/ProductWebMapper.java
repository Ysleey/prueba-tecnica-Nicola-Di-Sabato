package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.request.UpdateProductRequest;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.response.ProductResponse;

@Component
public class ProductWebMapper {

    public Product toDomain(CreateProductRequest request) {
        return new Product(
            null,
            request.name(),
            request.description(),
            request.price(),
            request.stock(),
            null,
            null
        );
    }

    public Product toDomain(UpdateProductRequest request) {
        return new Product(
            null,
            request.name(),
            request.description(),
            request.price(),
            request.stock(),
            null,
            null
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.id(),
            product.name(),
            product.description(),
            product.price(),
            product.stock(),
            product.createdAt(),
            product.updatedAt()
        );
    }
}