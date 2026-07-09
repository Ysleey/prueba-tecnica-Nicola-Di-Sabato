package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.prueba_tecnica_nicola.prueba.product.application.port.out.ProductRepositoryPort;
import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.out.persistence.repository.ProductJpaRepository;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository repository;

    public ProductPersistenceAdapter(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    // Métodos de conversión (Mappers)
    private Product toDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), 
                           entity.getPrice(), entity.getStock(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.id());
        entity.setName(product.name());
        entity.setDescription(product.description());
        entity.setPrice(product.price());
        entity.setStock(product.stock());
        return entity;
    }
     @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                         .stream()
                         .map(this::toDomain)
                         .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

}