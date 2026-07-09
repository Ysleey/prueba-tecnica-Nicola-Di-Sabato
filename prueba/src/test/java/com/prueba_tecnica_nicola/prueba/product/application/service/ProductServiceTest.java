package com.prueba_tecnica_nicola.prueba.product.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba_tecnica_nicola.prueba.product.application.port.out.ProductRepositoryPort;
import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.InvalidProductException;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private ProductService service;

    @Test
    void createProduct_success() {
        Product input = new Product(null, "Mouse", "Gaming", new BigDecimal("49.90"), 10, null, null);
        Product saved = new Product(1L, "Mouse", "Gaming", new BigDecimal("49.90"), 10,
            LocalDateTime.now(), null);
        when(repositoryPort.save(any(Product.class))).thenReturn(saved);

        Product result = service.createProduct(input);

        assertEquals(1L, result.id());
        assertEquals("Mouse", result.name());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repositoryPort).save(captor.capture());
        Product toPersist = captor.getValue();
        assertNull(toPersist.id());
        assertEquals("Mouse", toPersist.name());
    }

    @Test
    void updateProduct_nonExistingId_throwsNotFound() {
        Product input = new Product(null, "Teclado", "Mecanico", new BigDecimal("79.00"), 5, null, null);
        when(repositoryPort.findById(999L)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.updateProduct(999L, input));
        assertEquals("Producto con id 999 no encontrado", ex.getMessage());
    }

    @Test
    void createProduct_invalidPrice_throwsBadRequest() {
        Product input = new Product(null, "Monitor", "24 pulgadas", BigDecimal.ZERO, 3, null, null);

        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> service.createProduct(input));
        assertEquals("price debe ser mayor que 0", ex.getMessage());
    }

    @Test
    void createProduct_invalidStock_throwsBadRequest() {
        Product input = new Product(null, "Monitor", "24 pulgadas", new BigDecimal("199.99"), -1, null, null);

        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> service.createProduct(input));
        assertEquals("stock debe ser mayor o igual a 0", ex.getMessage());
    }

    @Test
    void deleteProduct_nonExistingId_throwsNotFound() {
        when(repositoryPort.findById(eq(123L))).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.deleteProduct(123L));
        assertEquals("Producto con id 123 no encontrado", ex.getMessage());
    }
}