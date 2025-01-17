package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.models.Authority;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Maak een nieuwe gebruiker aan
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        // Zet authorities (rollen) om naar een set van Authority-objecten
        Set<Authority> authorities = new HashSet<>();
        for (String authorityName : userDto.getAuthorities()) {
            Authority authority = new Authority(user, authorityName);
            authorities.add(authority);
        }
        user.setAuthorities(authorities);

        // Sla de gebruiker op in de database
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    // Haal een gebruiker op via de gebruikersnaam
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return convertToDto(user);
    }

    // Haal alle gebruikers op
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Voeg een nieuwe authority toe aan een gebruiker
    public void addAuthorityToUser(String username, String authorityName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Authority authority = new Authority(user, authorityName);
        user.addAuthority(authority);

        // Sla de wijzigingen op
        userRepository.save(user);
    }

    // Verwijder een authority van een gebruiker
    public void removeAuthorityFromUser(String username, String authorityName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Authority authority = user.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals(authorityName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Authority not found: " + authorityName));

        user.removeAuthority(authority);

        // Sla de wijzigingen op
        userRepository.save(user);
    }

    // Helper method: Zet een User om naar een UserDto
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        // Authorities worden direct omgezet naar een Set<String>
        dto.setAuthorities(user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet()));
        return dto;
    }
}
