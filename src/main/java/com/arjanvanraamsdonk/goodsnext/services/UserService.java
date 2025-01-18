package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Authority;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getRoles()))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        return new UserDto(user.getId(), user.getUsername(), null, user.getRoles());
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(userDto.getRoles()); // Rollen instellen
        userRepository.save(user);
        return new UserDto(user.getId(), user.getUsername(), null, user.getRoles());
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setRoles(userDto.getRoles()); // Rollen bijwerken
        userRepository.save(user);
        return new UserDto(user.getId(), user.getUsername(), null, user.getRoles());
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
