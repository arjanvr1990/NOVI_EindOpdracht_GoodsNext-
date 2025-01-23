package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users.stream()
                    .map(user -> new UserDto(user.getId(), user.getUsername(), user.getRoles()))
                    .collect(Collectors.toList());
        } else {
            throw new RecordNotFoundException("No users found");
        }
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new UserDto(user.getId(), user.getUsername(), null, user.getRoles());
        } else {
            throw new RecordNotFoundException("User with ID " + id + " not found");
        }
    }

    public UserDto createUser(UserInputDto userInputDto) {
        if (userInputDto != null) {
            User user = new User();
            user.setUsername(userInputDto.getUsername());
            user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
            user.setRoles(userInputDto.getAuthorities().stream().collect(Collectors.toSet()));

            if (userInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setEmail(userInputDto.getContactInfo().getEmail());
                contactInfo.setCity(userInputDto.getContactInfo().getCity());
                contactInfo.setPostalCode(userInputDto.getContactInfo().getPostalCode());
                contactInfo.setAddress(userInputDto.getContactInfo().getAddress());
                contactInfo.setPhoneNumber(userInputDto.getContactInfo().getPhoneNumber());
                user.setContactInfo(contactInfo);
            }

            User savedUser = userRepository.save(user);
            return new UserDto(savedUser.getId(), savedUser.getUsername(), null, savedUser.getRoles());
        } else {
            throw new IllegalArgumentException("Input data for creating user cannot be null");
        }
    }

    public void updateUser(Long id, UserInputDto userInputDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(userInputDto.getUsername());
            if (userInputDto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
            }
            user.setRoles(userInputDto.getAuthorities().stream().collect(Collectors.toSet()));

            if (userInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = user.getContactInfo();
                if (contactInfo == null) {
                    contactInfo = new ContactInfo();
                    user.setContactInfo(contactInfo);
                }
                contactInfo.setEmail(userInputDto.getContactInfo().getEmail());
                contactInfo.setCity(userInputDto.getContactInfo().getCity());
                contactInfo.setPostalCode(userInputDto.getContactInfo().getPostalCode());
                contactInfo.setAddress(userInputDto.getContactInfo().getAddress());
                contactInfo.setPhoneNumber(userInputDto.getContactInfo().getPhoneNumber());
            }

            userRepository.save(user);
        } else {
            throw new RecordNotFoundException("User with ID " + id + " not found");
        }
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new RecordNotFoundException("User not found with ID: " + id);
        }
    }
}
