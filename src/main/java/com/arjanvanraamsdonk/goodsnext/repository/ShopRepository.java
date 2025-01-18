package com.arjanvanraamsdonk.goodsnext.repository;


import com.arjanvanraamsdonk.goodsnext.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByShopNameContainingIgnoreCase(String shopName);
}

