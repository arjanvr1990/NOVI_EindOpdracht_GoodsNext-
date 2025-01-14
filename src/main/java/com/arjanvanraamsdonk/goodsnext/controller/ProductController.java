package com.arjanvanraamsdonk.goodsnext.controller;

import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "productName", required = false) String productName) {

        List<Product> products;

        if (productName == null){


            products = productRepository.findAll();


        } else {
            products = productRepository.findAllProductsByBrandEqualsIgnoreCase (productName);
        }

        return ResponseEntity.ok().body(products);

    }

    @GetMapping("/televisions/{id}")
    public ResponseEntity<Product> getTelevision(@PathVariable("id") Long id) {

        Optional<Product> television = productRepository.findById(id);

        if (television.isEmpty()){
            throw new RecordNotFoundException("No television found with id: " + id );

        } else {
            Product product1 = television.get();

            return ResponseEntity.ok().body(product1);
        }

    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        Product returnProduct = productRepository.save(product);

        return ResponseEntity.created(null).body(returnProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product newProduct) {


        Optional<Product> product = productRepository.findById(id);


        if (product.isEmpty()) {
            throw new RecordNotFoundException("No product found with id: " + id);
        } else {

            Product product1 = product.get();
            product1.setProductName(newProduct.getProductName());
            product1.setProductDescription(newProduct.getProductDescription());
            product1.setProductPrice(newProduct.getProductPrice());
            product1.setProductAvailability(newProduct.getProductAvailability());
            product1.setProductImg(newProduct.getProductImg());


            Product returnProduct = productRepository.save(product1);

            return ResponseEntity.ok().body(returnProduct);
        }
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<Product> updatePartialProduct(@PathVariable Long id, @RequestBody Product newProduct) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new RecordNotFoundException("No product found with id: " + id);
        } else {
            Product product1 = product.get();
            if (newProduct.getProductName() != null) {
                product1.setProductName(newProduct.getProductName());
            }
            if (newProduct.getProductDescription() != null) {
                product1.setProductDescription(newProduct.getProductDescription());
            }
            if (newProduct.getProductPrice() != null) {
                product1.setProductPrice(newProduct.getProductPrice());
            }
            if (newProduct.getProductAvailability() != null) {
                product1.setProductAvailability(newProduct.getProductAvailability());
            }
            if (newProduct.getProductImg() != null) {
                product1.setProductImg(newProduct.getProductImg());
            }

            Product returnProduct = productRepository.save(product1);
            return ResponseEntity.ok().body(returnProduct);
        }
    }


}



