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

    public List<ShopDto> getAllShops() {
        List<Shop> shopList = shopRepository.findAll();
        List<ShopDto> shopDtoList = new ArrayList<>();
        for (Shop shop : shopList) {
            shopDtoList.add(transferToDto(shop));
        }
        return shopDtoList;
    }

    public List<ShopDto> getAllShopsByName(String shopName) {
        List<Shop> shopList = shopRepository.findAllShopsByShopNameEqualsIgnoreCase(shopName);
        List<ShopDto> shopDtoList = new ArrayList<>();
        for (Shop shop : shopList) {
            shopDtoList.add(transferToDto(shop));
        }
        return shopDtoList;
    }

    public ShopDto getShopById(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent()) {
            return transferToDto(shopOptional.get());
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    public ShopDto addShop(ShopInputDto shopInputDto) {
        Shop shop = transferToShop(shopInputDto);

        if (shop.getContactInfo() != null) {
            ContactInfo savedContactInfo = contactInfoService.saveContactInfo(shop.getContactInfo());
            shop.setContactInfo(savedContactInfo);
        }

        Shop savedShop = shopRepository.save(shop);
        return transferToDto(savedShop);
    }

    public ShopDto updateShop(Long id, ShopInputDto shopInputDto) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent()) {
            Shop shopToUpdate = shopOptional.get();
            shopToUpdate.setShopName(shopInputDto.getShopName());

            if (shopInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = contactInfoService.transferToEntity(shopInputDto.getContactInfo());
                shopToUpdate.setContactInfo(contactInfo);
            }

            shopToUpdate.setLogo(shopInputDto.getLogo());
            Shop updatedShop = shopRepository.save(shopToUpdate);
            return transferToDto(updatedShop);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    public ShopDto updatePartialShop(Long id, ShopInputDto shopInputDto) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent()) {
            Shop shopToUpdate = shopOptional.get();

            if (shopInputDto.getShopName() != null) {
                shopToUpdate.setShopName(shopInputDto.getShopName());
            }
            if (shopInputDto.getLogo() != null) {
                shopToUpdate.setLogo(shopInputDto.getLogo());
            }
            if (shopInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = contactInfoService.transferToEntity(shopInputDto.getContactInfo());
                shopToUpdate.setContactInfo(contactInfo);
            }

            Shop updatedShop = shopRepository.save(shopToUpdate);
            return transferToDto(updatedShop);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    public void deleteShop(@RequestBody Long id) {
        if (shopRepository.existsById(id)) {
            shopRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No shop found with id: " + id);
        }
    }

    public Shop transferToShop(ShopInputDto dto) {
        Shop shop = new Shop();
        shop.setShopName(dto.getShopName());
        shop.setLogo(dto.getLogo());
        if (dto.getContactInfo() != null) {
            ContactInfo contactInfo = contactInfoService.transferToEntity(dto.getContactInfo());
            shop.setContactInfo(contactInfo);
        }
        return shop;
    }

    public ShopDto transferToDto(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        dto.setLogo(shop.getLogo());
        if (shop.getContactInfo() != null) {
            ContactInfoDto contactInfoDto = contactInfoService.transferToDto(shop.getContactInfo());
            dto.setContactInfo(contactInfoDto);
        }
        return dto;
    }
}
