package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.out.persistence.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    // Spring Data JPA ya nos da métodos como save, findById, findAll, delete, etc.
}