package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
    @NotBlank(message = "name es obligatorio")
    @Size(max = 150, message = "name debe tener maximo 150 caracteres")
    String name,

    String description,

    @NotNull(message = "price es obligatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "price debe ser mayor que 0")
    BigDecimal price,

    @NotNull(message = "stock es obligatorio")
    @Min(value = 0, message = "stock debe ser mayor o igual a 0")
    Integer stock
) {
}