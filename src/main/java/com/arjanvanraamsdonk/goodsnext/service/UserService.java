package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.UserDto;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.models.Role;
import com.arjanvanraamsdonk.goodsnext.repository.UserRepository;
import com.arjanvanraamsdonk.goodsnext.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return convertToDto(user);
    }


    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    // Helper method to convert User to UserDto
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        return dto;
    }

    // Helper method to convert UserDto to User
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        return user;
    }
}
