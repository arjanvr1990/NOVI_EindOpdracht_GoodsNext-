package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.services.UserService;
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

    @PostMapping("/{username}/authorities")
    public ResponseEntity<String> addAuthorityToUser(
            @PathVariable String username,
            @RequestParam String authorityName) {
        userService.addAuthorityToUser(username, authorityName);
        return ResponseEntity.ok("Authority added successfully");
    }

    @DeleteMapping("/{username}/authorities")
    public ResponseEntity<String> removeAuthorityFromUser(
            @PathVariable String username,
            @RequestParam String authorityName) {
        userService.removeAuthorityFromUser(username, authorityName);
        return ResponseEntity.ok("Authority removed successfully");
    }
}
