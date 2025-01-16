package com.arjanvanraamsdonk.goodsnext.controller;

import com.arjanvanraamsdonk.goodsnext.dto.UserDto;
import com.arjanvanraamsdonk.goodsnext.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{username}/roles")
    public ResponseEntity<String> addRoleToUser(
            @PathVariable String username,
            @RequestParam String roleName) {
        userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok("Role added successfully");
    }
}
