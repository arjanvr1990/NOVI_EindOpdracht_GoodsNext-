package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllProductsByProductNameEqualsIgnoreCase(String productName);
    List<Product> findByShop_ShopId(Long shopId);

    Optional<Product> findByShopAndProductName(Shop shop, String productName);

}
