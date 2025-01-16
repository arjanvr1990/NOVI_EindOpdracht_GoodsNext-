package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dto.RoleDto;
import com.arjanvanraamsdonk.goodsnext.dto.UserDto;
import com.arjanvanraamsdonk.goodsnext.dto.UserInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.Role;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repository.RoleRepository;
import com.arjanvanraamsdonk.goodsnext.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ContactInfoService contactInfoService;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, ContactInfoService contactInfoService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.contactInfoService = contactInfoService;
        this.roleRepository = roleRepository;
    }

    // Haal alle gebruikers op en converteer naar UserDto
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Haal een specifieke gebruiker op via ID en converteer naar UserDto
    public UserDto getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return convertToDto(userOptional.get());
        } else {
            throw new RecordNotFoundException("No user found with id: " + id);
        }
    }

    // Voeg een nieuwe gebruiker toe
    public UserDto addUser(UserInputDto userInputDto) {
        User user = convertToEntity(userInputDto);

        // Eerst sla de gebruiker op om een ID te genereren
        User savedUser = userRepository.save(user);

        // Koppel daarna de rollen
        if (userInputDto.getRoles() != null) {
            List<Role> roles = userInputDto.getRoles().stream()
                    .map(roleDto -> roleRepository.findById(roleDto.getRoleId())
                            .orElseThrow(() -> new RecordNotFoundException("Role not found with id: " + roleDto.getRoleId())))
                    .collect(Collectors.toList());
            savedUser.setRoles(roles);
            userRepository.save(savedUser); // Opslaan met gekoppelde rollen
        }

        return convertToDto(savedUser);
    }


    // Update een bestaande gebruiker
    public UserDto updateUser(Long id, UserInputDto userInputDto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            userToUpdate.setUsername(userInputDto.getUsername());
            userToUpdate.setPassword(userInputDto.getPassword());

            // Update ContactInfo
            if (userInputDto.getContactInfo() != null) {
                ContactInfo contactInfo = contactInfoService.transferToEntity(userInputDto.getContactInfo());
                userToUpdate.setContactInfo(contactInfo);
            }

            // Update Rollen
            if (userInputDto.getRoles() != null) {
                List<Role> roles = userInputDto.getRoles().stream()
                        .map(roleDto -> {
                            Role role = new Role();
                            role.setRoleId(roleDto.getRoleId());
                            role.setRoleName(roleDto.getRoleName());
                            return role;
                        })
                        .collect(Collectors.toList());
                userToUpdate.setRoles(roles);
            }

            User updatedUser = userRepository.save(userToUpdate);
            return convertToDto(updatedUser);
        } else {
            throw new RecordNotFoundException("No user found with id: " + id);
        }
    }

    // Verwijder een gebruiker via ID
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No user found with id: " + id);
        }
    }

    // Converteer een User naar een UserDto
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());

        // Zet ContactInfo om naar ContactInfoDto
        if (user.getContactInfo() != null) {
            ContactInfoDto contactInfoDto = contactInfoService.transferToDto(user.getContactInfo());
            userDto.setContactInfo(contactInfoDto);
        }

        // Zet Rollen om naar RoleDto's
        if (user.getRoles() != null) {
            List<RoleDto> roleDtos = user.getRoles().stream()
                    .map(role -> new RoleDto(role.getRoleId(), role.getRoleName()))
                    .collect(Collectors.toList());
            userDto.setRoles(roleDtos);
        }

        return userDto;
    }

    // Converteer een UserInputDto naar een User
    private User convertToEntity(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(userInputDto.getPassword());

        // Zet ContactInfoDto om naar ContactInfo
        if (userInputDto.getContactInfo() != null) {
            ContactInfo contactInfo = contactInfoService.transferToEntity(userInputDto.getContactInfo());
            user.setContactInfo(contactInfo);
        }

        // Zet RoleDto's om naar Role's
        if (userInputDto.getRoles() != null) {
            List<Role> roles = userInputDto.getRoles().stream()
                    .map(roleDto -> {
                        Role role = new Role();
                        role.setRoleId(roleDto.getRoleId());
                        role.setRoleName(roleDto.getRoleName());
                        return role;
                    })
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }

        return user;
    }
}
