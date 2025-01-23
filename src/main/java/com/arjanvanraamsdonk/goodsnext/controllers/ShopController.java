package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.ShopDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.services.ShopService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> getAllShops(@RequestParam(value = "shopName", required = false) String shopName) {
        List<ShopDto> shops;
        if (shopName == null) {
            shops = shopService.getAllShops();
        } else {
            shops = shopService.getAllShopsByName(shopName);
        }
        return ResponseEntity.ok().body(shops);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/shops/{id}")
    public ResponseEntity<ShopDto> getShop(@PathVariable("id") Long id) {
        ShopDto shop = shopService.getShopById(id);
        return ResponseEntity.ok().body(shop);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/shops")
    public ResponseEntity<ShopDto> addShop(@Valid @RequestBody ShopInputDto shopInputDto) {
        ShopDto savedShop = shopService.addShop(shopInputDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedShop.getShopId())
                .toUri();

        return ResponseEntity.created(location).body(savedShop);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/shops/{id}")
    public ResponseEntity<ShopDto> updateShop(@PathVariable Long id, @RequestBody ShopInputDto shopInputDto) {
        ShopDto updatedShop = shopService.updateShop(id, shopInputDto);
        return ResponseEntity.ok().body(updatedShop);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/shops/{id}")
    public ResponseEntity<ShopDto> updatePartialShop(@PathVariable Long id, @RequestBody ShopInputDto shopInputDto) {
        ShopDto updatedShop = shopService.updatePartialShop(id, shopInputDto);
        return ResponseEntity.ok().body(updatedShop);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

}
