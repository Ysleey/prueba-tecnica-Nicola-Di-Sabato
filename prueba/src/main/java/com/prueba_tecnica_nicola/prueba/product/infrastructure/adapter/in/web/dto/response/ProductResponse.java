package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}