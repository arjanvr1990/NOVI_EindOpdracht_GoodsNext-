package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserInputDto userInputDto) {
        UserDto createdUser = userService.createUser(userInputDto);

        URI location = URI.create(String.format("/api/users/%d", createdUser.getId()));
        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserInputDto userInputDto) {
        userService.updateUser(id, userInputDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.deleteUser(id)) {
            throw new RecordNotFoundException("User with ID " + id + " not found");
        }
        return ResponseEntity.noContent().build();
    }
}
