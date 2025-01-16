package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dto.ShopDto;
import com.arjanvanraamsdonk.goodsnext.dto.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
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
    private final ContactInfoService contactInfoService;

    public ShopService(ShopRepository shopRepository, ContactInfoService contactInfoService) {
        this.shopRepository = shopRepository;
        this.contactInfoService = contactInfoService;
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
    public Shop addShop(Shop shop) {
        if (shop.getContactInfo() != null) {
            // Sla de ContactInfo eerst op als deze niet null is
            ContactInfo savedContactInfo = contactInfoService.saveContactInfo(shop.getContactInfo());
            shop.setContactInfo(savedContactInfo);
        }

        // Sla daarna de Shop op
        return shopRepository.save(shop);
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

            // Zet ContactInfoDto om naar ContactInfo
            if (newShop.getContactInfo() != null) {
                ContactInfo contactInfo = contactInfoService.transferToEntity(newShop.getContactInfo());
                shopToUpdate.setContactInfo(contactInfo);
            }

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

        // Zet ContactInfoDto om naar ContactInfo
        if (dto.getContactInfo() != null) {
            ContactInfo contactInfo = contactInfoService.transferToEntity(dto.getContactInfo());
            shop.setContactInfo(contactInfo);
        }

        return shop;
    }


    // Converteer een Shop naar een ShopDto
    public ShopDto transferToDto(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        dto.setLogo(shop.getLogo());

        // Zet ContactInfo om naar ContactInfoDto
        if (shop.getContactInfo() != null) {
            ContactInfoDto contactInfoDto = contactInfoService.transferToDto(shop.getContactInfo());
            dto.setContactInfo(contactInfoDto);
        }

        return dto;
    }

}
