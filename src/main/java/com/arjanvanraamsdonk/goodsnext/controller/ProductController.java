package com.arjanvanraamsdonk.goodsnext.controller;

import com.arjanvanraamsdonk.goodsnext.dto.ProductInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
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
            products = productRepository.findAllProductsByProductNameEqualsIgnoreCase(productName);
        }

        return ResponseEntity.ok().body(products);

    }

    @GetMapping("/products/{id}")
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
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductInputDto productInputDto) {
        // Converteer de DTO naar een Product-entiteit
        Product product = new Product();
        product.setProductName(productInputDto.getProductName());
        product.setProductDescription(productInputDto.getProductDescription());
        product.setProductPrice(productInputDto.getProductPrice());
        product.setProductAvailability(productInputDto.getProductAvailability());
        product.setProductImg(productInputDto.getProductImg());

        // Sla het product op in de database
        Product savedProduct = productRepository.save(product);


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        // Retourneer een 201 Created response met de locatie en het opgeslagen product
        return ResponseEntity.created(location).body(savedProduct);
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



