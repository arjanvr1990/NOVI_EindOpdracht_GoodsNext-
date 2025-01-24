package com.arjanvanraamsdonk.goodsnext.repositories;


import com.arjanvanraamsdonk.goodsnext.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByShopNameContainingIgnoreCase(String shopName);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Shop s WHERE s.contactInfo.id = :contactInfoId")
    boolean existsByContactInfoId(@Param("contactInfoId") Long contactInfoId);

    @Query("SELECT s FROM Shop s WHERE s.contactInfo.id = :contactInfoId")
    Shop findByContactInfoId(@Param("contactInfoId") Long contactInfoId);
}

