package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba_tecnica_nicola.prueba.common.infrastructure.exception.GlobalExceptionHandler;
import com.prueba_tecnica_nicola.prueba.product.application.port.in.ProductServicePort;
import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.InvalidProductException;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.ProductNotFoundException;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.mapper.ProductWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@Import({ProductWebMapper.class, GlobalExceptionHandler.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductServicePort productService;

    @Test
    void createProduct_returns201() throws Exception {
        Product created = new Product(
            1L,
            "Mouse",
            "Gaming",
            new BigDecimal("49.90"),
            10,
            LocalDateTime.of(2026, 1, 1, 10, 0),
            null
        );
        when(productService.createProduct(any(Product.class))).thenReturn(created);

        String request = """
            {
              "name": "Mouse",
              "description": "Gaming",
              "price": 49.90,
              "stock": 10
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Mouse"))
            .andExpect(jsonPath("$.price").value(49.9))
            .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void getAllProducts_returns200WithPagination() throws Exception {
        Product product = new Product(
            1L,
            "Teclado",
            "Mecanico",
            new BigDecimal("79.00"),
            5,
            LocalDateTime.of(2026, 1, 1, 10, 0),
            null
        );
        when(productService.getAllProducts(0, 20)).thenReturn(List.of(product));
        when(productService.countProducts()).thenReturn(1L);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(20))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Teclado"));
    }

    @Test
    void getProductById_whenExists_returns200() throws Exception {
        Product product = new Product(
            2L,
            "Monitor",
            "24 pulgadas",
            new BigDecimal("199.99"),
            3,
            LocalDateTime.of(2026, 1, 1, 10, 0),
            null
        );
        when(productService.getProductById(2L)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 2L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("Monitor"));
    }

    @Test
    void getProductById_whenNotExists_returns404() throws Exception {
        doThrow(new ProductNotFoundException(999L)).when(productService).getProductById(999L);

        mockMvc.perform(get("/api/products/{id}", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Producto con id 999 no encontrado"));
    }

    @Test
    void updateProduct_returns200() throws Exception {
        Product updated = new Product(
            3L,
            "Webcam",
            "HD",
            new BigDecimal("39.99"),
            7,
            LocalDateTime.of(2026, 1, 1, 10, 0),
            LocalDateTime.of(2026, 1, 2, 10, 0)
        );
        when(productService.updateProduct(eq(3L), any(Product.class))).thenReturn(updated);

        String request = objectMapper.writeValueAsString(new java.util.LinkedHashMap<String, Object>() {{
            put("name", "Webcam");
            put("description", "HD");
            put("price", 39.99);
            put("stock", 7);
        }});

        mockMvc.perform(put("/api/products/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Webcam"));
    }

    @Test
    void deleteProduct_returns204() throws Exception {
        doNothing().when(productService).deleteProduct(5L);

        mockMvc.perform(delete("/api/products/{id}", 5L))
            .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_withInvalidBody_returns400() throws Exception {
        String request = """
            {
              "name": "",
              "description": "X",
              "price": 0,
              "stock": -1
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Error de validacion"));
    }

    @Test
    void updateProduct_withInvalidBody_returns400() throws Exception {
        String request = """
            {
              "name": "",
              "description": "X",
              "price": 0,
              "stock": -1
            }
            """;

        mockMvc.perform(put("/api/products/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Error de validacion"));
    }

    @Test
    void getAllProducts_withInvalidPage_returns400() throws Exception {
        doThrow(new InvalidProductException("page debe ser >= 0"))
            .when(productService).getAllProducts(-1, 20);

        mockMvc.perform(get("/api/products")
                .param("page", "-1")
                .param("size", "20"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("page debe ser >= 0"));
    }

    @Test
    void getAllProducts_withInvalidSize_returns400() throws Exception {
        doThrow(new InvalidProductException("size debe ser > 0"))
            .when(productService).getAllProducts(0, 0);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("size debe ser > 0"));
    }

    @Test
    void getProductById_withInvalidPathType_returns400() throws Exception {
        mockMvc.perform(get("/api/products/{id}", "abc"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("El parametro id tiene un formato invalido"));
    }

    @Test
    void createProduct_withMalformedJson_returns400() throws Exception {
        String request = "{ \"name\": \"Mouse\", \"price\": }";

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Body JSON invalido"));
    }

    @Test
    void createProduct_withUnexpectedError_returns500() throws Exception {
        doThrow(new RuntimeException("boom")).when(productService).createProduct(any(Product.class));

        String request = """
            {
              "name": "Mouse",
              "description": "Gaming",
              "price": 49.90,
              "stock": 10
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.message").value("Error interno del servidor"));
    }
}