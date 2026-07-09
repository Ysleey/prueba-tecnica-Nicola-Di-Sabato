package com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_tecnica_nicola.prueba.product.application.port.in.ProductServicePort;
import com.prueba_tecnica_nicola.prueba.product.domain.Product;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.request.UpdateProductRequest;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.response.ProductPageResponse;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.dto.response.ProductResponse;
import com.prueba_tecnica_nicola.prueba.product.infrastructure.adapter.in.web.mapper.ProductWebMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServicePort productService;
    private final ProductWebMapper webMapper;

    public ProductController(ProductServicePort productService, ProductWebMapper webMapper) {
        this.productService = productService;
        this.webMapper = webMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product created = productService.createProduct(webMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        List<ProductResponse> content = productService.getAllProducts(page, size)
            .stream()
            .map(webMapper::toResponse)
            .toList();

        long total = productService.countProducts();
        int totalPages = (int) Math.ceil((double) total / size);

        ProductPageResponse response = new ProductPageResponse(content, page, size, total, totalPages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(webMapper.toResponse(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        Product updated = productService.updateProduct(id, webMapper.toDomain(request));
        return ResponseEntity.ok(webMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}