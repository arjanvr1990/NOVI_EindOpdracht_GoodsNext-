package com.arjanvanraamsdonk.goodsnext.repositories;


import com.arjanvanraamsdonk.goodsnext.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllProductsByProductNameEqualsIgnoreCase(String productName);

}

