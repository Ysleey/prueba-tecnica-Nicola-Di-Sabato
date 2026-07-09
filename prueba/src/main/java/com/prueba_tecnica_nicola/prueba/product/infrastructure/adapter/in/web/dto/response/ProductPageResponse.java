package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.response;

import java.util.List;

public record ProductPageResponse(
    List<ProductResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
}