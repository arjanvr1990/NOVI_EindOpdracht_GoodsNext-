package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
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
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final PhotoUploadRepository photoUploadRepository;

    public ShopService(ShopRepository shopRepository,
                       ContactInfoRepository contactInfoRepository,
                       PhotoUploadRepository photoUploadRepository) {
        this.shopRepository = shopRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.photoUploadRepository = photoUploadRepository;
    }

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        return shops.stream().map(this::fromShop).collect(Collectors.toList());
    }

    public List<ShopDto> getAllShopsByName(String shopName) {
        List<Shop> shops = shopRepository.findByShopNameContainingIgnoreCase(shopName);
        return shops.stream().map(this::fromShop).collect(Collectors.toList());
    }

    public ShopDto getShopById(Long id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop != null) {
            return fromShop(shop);
        } else {
            throw new RecordNotFoundException("Shop not found with ID: " + id);
        }
    }

    public ShopDto addShop(ShopInputDto shopInputDto) {
        if (shopInputDto != null) {
            Shop shop = toShop(shopInputDto);

            if (shopInputDto.getLogo() != null) {
                PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo()).orElse(null);
                if (logo != null) {
                    shop.setLogo(logo);
                } else {
                    throw new RecordNotFoundException("PhotoUpload not found with ID: " + shopInputDto.getLogo());
                }
            }

            Shop savedShop = shopRepository.save(shop);
            return fromShop(savedShop);
        } else {
            throw new IllegalArgumentException("Input data for creating shop cannot be null");
        }
    }


    public ShopDto updateShop(Long id, ShopInputDto shopInputDto) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop != null) {
            shop.setShopName(shopInputDto.getShopName());

            if (shopInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = toContactInfo(shopInputDto.getContactInfo());
                shop.setContactInfo(contactInfoRepository.save(contactInfo));
            }

            if (shopInputDto.getLogo() != null) {
                PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo()).orElse(null);
                if (logo != null) {
                    shop.setLogo(logo);
                } else {
                    throw new RecordNotFoundException("PhotoUpload not found with ID: " + shopInputDto.getLogo());
                }
            }

            Shop updatedShop = shopRepository.save(shop);
            return fromShop(updatedShop);
        } else {
            throw new RecordNotFoundException("Shop not found with ID: " + id);
        }
    }

    public ShopDto updatePartialShop(Long id, ShopInputDto shopInputDto) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Shop not found with ID: " + id));

        if (shopInputDto.getShopName() != null) {
            shop.setShopName(shopInputDto.getShopName());
        }

        if (shopInputDto.getContactInfo() != null) {
            ContactInfo contactInfo = toContactInfo(shopInputDto.getContactInfo());
            shop.setContactInfo(contactInfoRepository.save(contactInfo));
        }

        if (shopInputDto.getLogo() != null) {
            PhotoUpload logo = photoUploadRepository.findById(shopInputDto.getLogo())
                    .orElseThrow(() -> new RecordNotFoundException("PhotoUpload not found with ID: " + shopInputDto.getLogo()));
            shop.setLogo(logo);
        }

        Shop updatedShop = shopRepository.save(shop);
        return fromShop(updatedShop);
    }

    public void deleteShop(Long id) {
        if (shopRepository.existsById(id)) {
            shopRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Shop not found with ID: " + id);
        }
    }


    private ShopDto fromShop(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        if (shop.getContactInfo() != null) {
            dto.setContactInfo(fromContactInfo(shop.getContactInfo()));
        }
        if (shop.getLogo() != null) {
            dto.setLogo(String.valueOf(shop.getLogo().getUploadId()));
        }
        return dto;
    }

    private Shop toShop(ShopInputDto shopInputDto) {
        Shop shop = new Shop();
        shop.setShopName(shopInputDto.getShopName());
        if (shopInputDto.getContactInfo() != null) {
            shop.setContactInfo(toContactInfo(shopInputDto.getContactInfo()));
        }
        return shop;
    }

    private ContactInfoDto fromContactInfo(ContactInfo contactInfo) {
        ContactInfoDto dto = new ContactInfoDto();
        dto.setEmail(contactInfo.getEmail());
        dto.setCity(contactInfo.getCity());
        dto.setPostalCode(contactInfo.getPostalCode());
        dto.setAddress(contactInfo.getAddress());
        dto.setPhoneNumber(contactInfo.getPhoneNumber());
        return dto;
    }

    private ContactInfo toContactInfo(ContactInfoDto dto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(dto.getEmail());
        contactInfo.setCity(dto.getCity());
        contactInfo.setPostalCode(dto.getPostalCode());
        contactInfo.setAddress(dto.getAddress());
        contactInfo.setPhoneNumber(dto.getPhoneNumber());
        return contactInfo;
    }
}
