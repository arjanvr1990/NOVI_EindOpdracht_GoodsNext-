package com.arjanvanraamsdonk.goodsnext.repository;


import com.arjanvanraamsdonk.goodsnext.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // query-methodes toevoegen
}

