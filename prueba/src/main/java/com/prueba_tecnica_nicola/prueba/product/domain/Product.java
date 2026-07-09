package com.prueba_tecnica_nicola.prueba.product.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
