package com.arjanvanraamsdonk.goodsnext.repositories;


import com.arjanvanraamsdonk.goodsnext.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByShopNameContainingIgnoreCase(String shopName);
}

