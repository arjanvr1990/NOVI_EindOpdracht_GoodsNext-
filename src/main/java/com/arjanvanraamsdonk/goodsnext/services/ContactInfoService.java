package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.ContactInfoRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;

    // Constructor-injectie van beide repositories
    public ContactInfoService(ContactInfoRepository contactInfoRepository, UserRepository userRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.userRepository = userRepository;
    }

    public ContactInfo saveContactInfo(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    public List<ContactInfoDto> getAllContactInfo() {
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        return contactInfoList.stream().map(this::transferToDto).collect(Collectors.toList());
    }

    public ContactInfoDto getContactInfoById(Long id) {
        Optional<ContactInfo> contactInfo = contactInfoRepository.findById(id);
        if (contactInfo.isPresent()) {
            return transferToDto(contactInfo.get());
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
    }

    public ContactInfoDto getContactInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        if (user.getContactInfo() == null) {
            throw new RecordNotFoundException("No contact info found for user");
        }
        return transferToDto(user.getContactInfo());
    }

    public ContactInfoDto updateContactInfoByUsername(String username, ContactInfoDto contactInfoDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        ContactInfo contactInfo = user.getContactInfo();
        if (contactInfo == null) {
            contactInfo = new ContactInfo();
            user.setContactInfo(contactInfo);
        }

        contactInfo.setEmail(contactInfoDto.getEmail());
        contactInfo.setCity(contactInfoDto.getCity());
        contactInfo.setPostalCode(contactInfoDto.getPostalCode());
        contactInfo.setAddress(contactInfoDto.getAddress());
        contactInfo.setPhoneNumber(contactInfoDto.getPhoneNumber());

        userRepository.save(user);
        return transferToDto(contactInfo);
    }

    public ContactInfoDto addContactInfo(ContactInfoDto dto) {
        ContactInfo contactInfo = transferToEntity(dto);
        contactInfoRepository.save(contactInfo);
        return transferToDto(contactInfo);
    }

    public ContactInfoDto updateContactInfo(Long id, ContactInfoDto dto) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(id);
        if (contactInfoOptional.isPresent()) {
            ContactInfo contactInfo = contactInfoOptional.get();
            contactInfo.setEmail(dto.getEmail());
            contactInfo.setCity(dto.getCity());
            contactInfo.setPostalCode(dto.getPostalCode());
            contactInfo.setAddress(dto.getAddress());
            contactInfo.setPhoneNumber(dto.getPhoneNumber());
            contactInfoRepository.save(contactInfo);
            return transferToDto(contactInfo);
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
    }

    public void deleteContactInfo(Long id) {
        if (contactInfoRepository.existsById(id)) {
            contactInfoRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No ContactInfo found with id: " + id);
        }
    }

    public ContactInfo transferToEntity(ContactInfoDto dto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(dto.getEmail());
        contactInfo.setCity(dto.getCity());
        contactInfo.setPostalCode(dto.getPostalCode());
        contactInfo.setAddress(dto.getAddress());
        contactInfo.setPhoneNumber(dto.getPhoneNumber());
        return contactInfo;
    }

    public ContactInfoDto transferToDto(ContactInfo contactInfo) {
        ContactInfoDto dto = new ContactInfoDto();
        dto.setEmail(contactInfo.getEmail());
        dto.setCity(contactInfo.getCity());
        dto.setPostalCode(contactInfo.getPostalCode());
        dto.setAddress(contactInfo.getAddress());
        dto.setPhoneNumber(contactInfo.getPhoneNumber());
        return dto;
    }
}
