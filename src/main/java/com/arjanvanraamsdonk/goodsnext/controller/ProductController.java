package com.arjanvanraamsdonk.goodsnext.controller;

import com.arjanvanraamsdonk.goodsnext.dto.ProductDto;
import com.arjanvanraamsdonk.goodsnext.dto.ProductInputDto;
import com.arjanvanraamsdonk.goodsnext.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(value = "productName", required = false) String productName) {
        List<ProductDto> products;
        if (productName == null) {
            products = productService.getAllProducts();
        } else {
            products = productService.getAllProductsByName(productName);
        }
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductInputDto productInputDto) {
        ProductDto savedProduct = productService.addProduct(productInputDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getProductId())
                .toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductInputDto productInputDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productInputDto);
        return ResponseEntity.ok().body(updatedProduct);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductDto> updatePartialProduct(@PathVariable Long id, @RequestBody ProductInputDto productInputDto) {
        ProductDto updatedProduct = productService.updatePartialProduct(id, productInputDto);
        return ResponseEntity.ok().body(updatedProduct);
    }
}
