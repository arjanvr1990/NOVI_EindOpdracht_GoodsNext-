package com.arjanvanraamsdonk.goodsnext.service;


import com.arjanvanraamsdonk.goodsnext.model.Product;
import com.arjanvanraamsdonk.goodsnext.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

};
