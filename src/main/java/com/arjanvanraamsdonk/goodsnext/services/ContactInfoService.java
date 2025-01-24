package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.ContactInfoInUseException;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.ContactInfoRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.ShopRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository, UserRepository userRepository, ShopRepository shopRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
    }

    public List<ContactInfoDto> getAllContactInfo() {
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        return contactInfoList.stream()
                .map(this::fromContactInfo)
                .collect(Collectors.toList());
    }

    public ContactInfoDto getContactInfoById(Long id) {
        ContactInfo contactInfo = contactInfoRepository.findById(id).orElse(null);
        if (contactInfo != null) {
            ContactInfoDto dto = new ContactInfoDto();
            dto.setEmail(contactInfo.getEmail());
            dto.setCity(contactInfo.getCity());
            dto.setPostalCode(contactInfo.getPostalCode());
            dto.setAddress(contactInfo.getAddress());
            dto.setPhoneNumber(contactInfo.getPhoneNumber());
            return dto;
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
    }



    public ContactInfoDto getContactInfoByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            ContactInfo contactInfo = user.getContactInfo();
            if (contactInfo != null) {
                ContactInfoDto dto = new ContactInfoDto();
                dto.setEmail(contactInfo.getEmail());
                dto.setCity(contactInfo.getCity());
                dto.setPostalCode(contactInfo.getPostalCode());
                dto.setAddress(contactInfo.getAddress());
                dto.setPhoneNumber(contactInfo.getPhoneNumber());
                return dto;
            } else {
                throw new RecordNotFoundException("No contact info found for user with username: " + username);
            }
        } else {
            throw new RecordNotFoundException("User not found with username: " + username);
        }
    }


    public ContactInfoDto updateContactInfoByUsername(String username, ContactInfoInputDto contactInfoInputDto) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            ContactInfo contactInfo = user.getContactInfo();
            if (contactInfo == null) {
                contactInfo = new ContactInfo();
                user.setContactInfo(contactInfo);
            }

            contactInfo.setEmail(contactInfoInputDto.getEmail());
            contactInfo.setCity(contactInfoInputDto.getCity());
            contactInfo.setPostalCode(contactInfoInputDto.getPostalCode());
            contactInfo.setAddress(contactInfoInputDto.getAddress());
            contactInfo.setPhoneNumber(contactInfoInputDto.getPhoneNumber());

            userRepository.save(user);

            ContactInfoDto dto = new ContactInfoDto();
            dto.setEmail(contactInfo.getEmail());
            dto.setCity(contactInfo.getCity());
            dto.setPostalCode(contactInfo.getPostalCode());
            dto.setAddress(contactInfo.getAddress());
            dto.setPhoneNumber(contactInfo.getPhoneNumber());
            return dto;
        } else {
            throw new RecordNotFoundException("User not found with username: " + username);
        }
    }


    public ContactInfoDto addContactInfo(ContactInfoInputDto contactInfoInputDto) {
        if (contactInfoInputDto != null) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setEmail(contactInfoInputDto.getEmail());
            contactInfo.setCity(contactInfoInputDto.getCity());
            contactInfo.setPostalCode(contactInfoInputDto.getPostalCode());
            contactInfo.setAddress(contactInfoInputDto.getAddress());
            contactInfo.setPhoneNumber(contactInfoInputDto.getPhoneNumber());

            contactInfoRepository.save(contactInfo);

            ContactInfoDto dto = new ContactInfoDto();
            dto.setEmail(contactInfo.getEmail());
            dto.setCity(contactInfo.getCity());
            dto.setPostalCode(contactInfo.getPostalCode());
            dto.setAddress(contactInfo.getAddress());
            dto.setPhoneNumber(contactInfo.getPhoneNumber());
            return dto;
        } else {
            throw new IllegalArgumentException("Input data for creating contact info cannot be null");
        }
    }


    public ContactInfoDto updateContactInfo(Long id, ContactInfoInputDto contactInfoInputDto) {
        ContactInfo contactInfo = contactInfoRepository.findById(id).orElse(null);
        if (contactInfo != null) {
            contactInfo.setEmail(contactInfoInputDto.getEmail());
            contactInfo.setCity(contactInfoInputDto.getCity());
            contactInfo.setPostalCode(contactInfoInputDto.getPostalCode());
            contactInfo.setAddress(contactInfoInputDto.getAddress());
            contactInfo.setPhoneNumber(contactInfoInputDto.getPhoneNumber());

            contactInfoRepository.save(contactInfo);

            ContactInfoDto dto = new ContactInfoDto();
            dto.setEmail(contactInfo.getEmail());
            dto.setCity(contactInfo.getCity());
            dto.setPostalCode(contactInfo.getPostalCode());
            dto.setAddress(contactInfo.getAddress());
            dto.setPhoneNumber(contactInfo.getPhoneNumber());
            return dto;
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
    }



    public void deleteContactInfo(Long id) {
        if (userRepository.existsByContactInfoId(id)) {
            User user = userRepository.findByContactInfoId(id);
            throw new ContactInfoInUseException("ContactInfo is connected to user (User ID: " + user.getId() + "). Delete this user and it deletes as well the ContactInfo");
        }

        if (shopRepository.existsByContactInfoId(id)) {
            Shop shop = shopRepository.findByContactInfoId(id);
            throw new ContactInfoInUseException("ContactInfo is connected to shop (Shop ID: " + shop.getShopId() + "). Delete this shop and it deletes as well the ContactInfo");
        }

        if (contactInfoRepository.existsById(id)) {
            contactInfoRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
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

    private ContactInfo toContactInfo(ContactInfoInputDto inputDto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(inputDto.getEmail());
        contactInfo.setCity(inputDto.getCity());
        contactInfo.setPostalCode(inputDto.getPostalCode());
        contactInfo.setAddress(inputDto.getAddress());
        contactInfo.setPhoneNumber(inputDto.getPhoneNumber());
        return contactInfo;
    }

    private void updateContactInfoFields(ContactInfo contactInfo, ContactInfoInputDto inputDto) {
        contactInfo.setEmail(inputDto.getEmail());
        contactInfo.setCity(inputDto.getCity());
        contactInfo.setPostalCode(inputDto.getPostalCode());
        contactInfo.setAddress(inputDto.getAddress());
        contactInfo.setPhoneNumber(inputDto.getPhoneNumber());
    }
}
