package com.arjanvanraamsdonk.goodsnext.controller;


import com.arjanvanraamsdonk.goodsnext.dto.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.repository.ShopRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ShopController {

    private final ShopRepository shopRepository;

    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getAllShops(@RequestParam(value = "shopName", required = false) String shopName) {

        List<Shop> shops;

        if (shopName == null) {
            shops = shopRepository.findAll();
        } else {
            shops = shopRepository.findAllShopsByShopNameEqualsIgnoreCase(shopName);
        }

        return ResponseEntity.ok().body(shops);
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable("id") Long id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new RecordNotFoundException("No shop found with id: " + id);
        } else {
            return ResponseEntity.ok().body(shop.get());
        }
    }

    @PostMapping("/shops")
    public ResponseEntity<Shop> addShop(@Valid @RequestBody ShopInputDto shopInputDto) {
        // Converteer de DTO naar een Shop-entiteit
        Shop shop = new Shop();
        shop.setShopName(shopInputDto.getShopName());
        shop.setContactInfo(shopInputDto.getContactInfo());
        shop.setLogo(shopInputDto.getLogo());

        // Sla de shop op in de database
        Shop savedShop = shopRepository.save(shop);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedShop.getShopId())
                .toUri();

        // Retourneer een 201 Created response met de locatie en de opgeslagen shop
        return ResponseEntity.created(location).body(savedShop);
    }

    @PutMapping("/shops/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody Shop newShop) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new RecordNotFoundException("No shop found with id: " + id);
        } else {
            Shop shopToUpdate = shop.get();
            shopToUpdate.setShopName(newShop.getShopName());
            shopToUpdate.setContactInfo(newShop.getContactInfo());
            shopToUpdate.setLogo(newShop.getLogo());

            Shop updatedShop = shopRepository.save(shopToUpdate);
            return ResponseEntity.ok().body(updatedShop);
        }
    }

    @PatchMapping("/shops/{id}")
    public ResponseEntity<Shop> updatePartialShop(@PathVariable Long id, @RequestBody Shop newShop) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new RecordNotFoundException("No shop found with id: " + id);
        } else {
            Shop shopToUpdate = shop.get();
            if (newShop.getShopName() != null) {
                shopToUpdate.setShopName(newShop.getShopName());
            }
            if (newShop.getContactInfo() != null) {
                shopToUpdate.setContactInfo(newShop.getContactInfo());
            }
            if (newShop.getLogo() != null) {
                shopToUpdate.setLogo(newShop.getLogo());
            }

            Shop updatedShop = shopRepository.save(shopToUpdate);
            return ResponseEntity.ok().body(updatedShop);
        }
    }
}
