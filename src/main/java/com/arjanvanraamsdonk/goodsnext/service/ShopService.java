package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.ShopDto;
import com.arjanvanraamsdonk.goodsnext.dto.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    // Haal alle shops op en vertaal naar ShopDto's
    public List<ShopDto> getAllShops() {
        List<Shop> shopList = shopRepository.findAll();
        List<ShopDto> shopDtoList = new ArrayList<>();

        for (Shop shop : shopList) {
            ShopDto dto = transferToDto(shop);
            shopDtoList.add(dto);
        }
        return shopDtoList;
    }

    // Haal alle shops op met een specifieke naam en vertaal naar ShopDto's
    public List<ShopDto> getAllShopsByName(String shopName) {
        List<Shop> shopList = shopRepository.findAllShopsByShopNameEqualsIgnoreCase(shopName);
        List<ShopDto> shopDtoList = new ArrayList<>();

        for (Shop shop : shopList) {
            ShopDto dto = transferToDto(shop);
            shopDtoList.add(dto);
        }
        return shopDtoList;
    }

    // Haal één shop op via ID en vertaal naar een ShopDto
    public ShopDto getShopById(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            return transferToDto(shop);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    // Voeg een nieuwe shop toe en retourneer een ShopDto
    public ShopDto addShop(ShopInputDto dto) {
        Shop shop = transferToShop(dto);
        shopRepository.save(shop);
        return transferToDto(shop);
    }

    // Verwijder een shop via ID
    public void deleteShop(@RequestBody Long id) {
        if (shopRepository.existsById(id)) {
            shopRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    // Werk een shop volledig bij en retourneer een bijgewerkte ShopDto
    public ShopDto updateShop(Long id, ShopInputDto newShop) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent()) {
            Shop shopToUpdate = shopOptional.get();
            shopToUpdate.setShopName(newShop.getShopName());
            shopToUpdate.setContactInfo(newShop.getContactInfo());
            shopToUpdate.setLogo(newShop.getLogo());
            Shop updatedShop = shopRepository.save(shopToUpdate);
            return transferToDto(updatedShop);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    // Converteer een ShopInputDto naar een Shop
    public Shop transferToShop(ShopInputDto dto) {
        Shop shop = new Shop();
        shop.setShopName(dto.getShopName());
        shop.setLogo(dto.getLogo());
        shop.setContactInfo(dto.getContactInfo());
        return shop;
    }

    // Converteer een Shop naar een ShopDto
    public ShopDto transferToDto(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        dto.setLogo(shop.getLogo());
        dto.setContactInfo(shop.getContactInfo());
        return dto;
    }
}
