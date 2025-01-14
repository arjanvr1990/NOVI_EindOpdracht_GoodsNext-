package com.arjanvanraamsdonk.goodsnext.controller;


import com.arjanvanraamsdonk.goodsnext.model.Product;
import com.arjanvanraamsdonk.goodsnext.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products")
    public ResponseEntity<String> getAllProducts() {


        return ResponseEntity.ok("products");

    }

    @GetMapping("/products/{id}")
    public ResponseEntity<String> getProduct(@PathVariable("id") int id) {


        return ResponseEntity.ok("product with id: " + id);

    }

    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@RequestBody String product) {


        return ResponseEntity.created(null).body("product");

    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable int id, @RequestBody String product) {

        // Return een 204 status
        return ResponseEntity.noContent().build();

    }
}
