package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.ContactInfoRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository, UserRepository userRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.userRepository = userRepository;
    }

    public List<ContactInfoDto> getAllContactInfo() {
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        return contactInfoList.stream()
                .map(this::fromContactInfo)
                .collect(Collectors.toList());
    }

    public ContactInfoDto getContactInfoById(Long id) {
        ContactInfo contactInfo = contactInfoRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No ContactInfo found with id: " + id));
        return fromContactInfo(contactInfo);
    }

    public ContactInfoDto getContactInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));
        ContactInfo contactInfo = user.getContactInfo();
        if (contactInfo == null) {
            throw new RecordNotFoundException("No contact info found for user with username: " + username);
        }
        return fromContactInfo(contactInfo);
    }

    public ContactInfoDto updateContactInfoByUsername(String username, ContactInfoInputDto contactInfoInputDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));

        ContactInfo contactInfo = user.getContactInfo();
        if (contactInfo == null) {
            contactInfo = toContactInfo(contactInfoInputDto);
            user.setContactInfo(contactInfo);
        } else {
            updateContactInfoFields(contactInfo, contactInfoInputDto);
        }

        userRepository.save(user);
        return fromContactInfo(contactInfo);
    }

    public ContactInfoDto addContactInfo(ContactInfoInputDto contactInfoInputDto) {
        if (contactInfoInputDto == null) {
            throw new IllegalArgumentException("Input data for creating contact info cannot be null");
        }
        ContactInfo contactInfo = toContactInfo(contactInfoInputDto);
        contactInfoRepository.save(contactInfo);
        return fromContactInfo(contactInfo);
    }

    public ContactInfoDto updateContactInfo(Long id, ContactInfoInputDto contactInfoInputDto) {
        ContactInfo contactInfo = contactInfoRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No ContactInfo found with id: " + id));
        updateContactInfoFields(contactInfo, contactInfoInputDto);
        contactInfoRepository.save(contactInfo);
        return fromContactInfo(contactInfo);
    }

    public void deleteContactInfo(Long id) {
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
