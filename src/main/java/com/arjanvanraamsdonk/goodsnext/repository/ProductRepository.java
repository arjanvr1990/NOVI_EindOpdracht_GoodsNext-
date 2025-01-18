package com.arjanvanraamsdonk.goodsnext.repository;


import com.arjanvanraamsdonk.goodsnext.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllProductsByBrandEqualsIgnoreCase(String brand);

}

