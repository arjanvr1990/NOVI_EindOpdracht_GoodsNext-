package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ShopDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.repositories.ContactInfoRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.PhotoUploadRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final PhotoUploadRepository photoUploadRepository;
    private final ContactInfoService contactInfoService;

    public ShopService(ShopRepository shopRepository,
                       ContactInfoRepository contactInfoRepository,
                       PhotoUploadRepository photoUploadRepository,
                       ContactInfoService contactInfoService) {
        this.shopRepository = shopRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.photoUploadRepository = photoUploadRepository;
        this.contactInfoService = contactInfoService;
    }

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        return shops.stream().map(this::transferToDto).collect(Collectors.toList());
    }

    public List<ShopDto> getAllShopsByName(String shopName) {
        List<Shop> shops = shopRepository.findByShopNameContainingIgnoreCase(shopName);
        return shops.stream().map(this::transferToDto).collect(Collectors.toList());
    }


    public ShopDto getShopById(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Shop not found with ID: " + id));
        return transferToDto(shop);
    }


    public ShopDto addShop(ShopInputDto shopInputDto) {
        Shop shop = new Shop();
        shop.setShopName(shopInputDto.getShopName());

        if (shopInputDto.getContactInfo() != null) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setAddress(shopInputDto.getContactInfo().getAddress());
            contactInfo.setCity(shopInputDto.getContactInfo().getCity());
            contactInfo.setEmail(shopInputDto.getContactInfo().getEmail());
            contactInfo.setPhoneNumber(shopInputDto.getContactInfo().getPhoneNumber());
            contactInfo.setPostalCode(shopInputDto.getContactInfo().getPostalCode());
            contactInfo = contactInfoRepository.save(contactInfo);
            shop.setContactInfo(contactInfo);
        }

        if (shopInputDto.getLogo() != null) {
            PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo())
                    .orElseThrow(() -> new RuntimeException("PhotoUpload not found"));
            shop.setLogo(logo);
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
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setAddress(shopInputDto.getContactInfo().getAddress());
                contactInfo.setCity(shopInputDto.getContactInfo().getCity());
                contactInfo.setEmail(shopInputDto.getContactInfo().getEmail());
                contactInfo.setPhoneNumber(shopInputDto.getContactInfo().getPhoneNumber());
                contactInfo.setPostalCode(shopInputDto.getContactInfo().getPostalCode());
                shopToUpdate.setContactInfo(contactInfo);
            }

            if (shopInputDto.getLogo() != null) {
                PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo())
                        .orElseThrow(() -> new RuntimeException("PhotoUpload not found"));
                shopToUpdate.setLogo(logo);
            }

            Shop updatedShop = shopRepository.save(shopToUpdate);
            return transferToDto(updatedShop);
        } else {
            throw new RuntimeException("Shop not found with ID: " + id);
        }
    }

    public ShopDto updatePartialShop(Long id, ShopInputDto shopInputDto) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if (shopOptional.isPresent()) {
            Shop shopToUpdate = shopOptional.get();

            if (shopInputDto.getShopName() != null) {
                shopToUpdate.setShopName(shopInputDto.getShopName());
            }

            if (shopInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setAddress(shopInputDto.getContactInfo().getAddress());
                contactInfo.setCity(shopInputDto.getContactInfo().getCity());
                contactInfo.setEmail(shopInputDto.getContactInfo().getEmail());
                contactInfo.setPhoneNumber(shopInputDto.getContactInfo().getPhoneNumber());
                contactInfo.setPostalCode(shopInputDto.getContactInfo().getPostalCode());
                shopToUpdate.setContactInfo(contactInfo);
            }

            if (shopInputDto.getLogo() != null) {
                PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo())
                        .orElseThrow(() -> new RuntimeException("PhotoUpload not found"));
                shopToUpdate.setLogo(logo);
            }

            Shop updatedShop = shopRepository.save(shopToUpdate);
            return transferToDto(updatedShop);
        } else {
            throw new RuntimeException("Shop not found with ID: " + id);
        }
    }

    private ShopDto transferToDto(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        dto.setContactInfo(contactInfoService.transferToDto(shop.getContactInfo()));

        if (shop.getLogo() != null) {
            dto.setLogo(String.valueOf(shop.getLogo().getUploadId()));
        }

        return dto;
    }
}
