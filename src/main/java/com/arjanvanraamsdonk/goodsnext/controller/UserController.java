package com.arjanvanraamsdonk.goodsnext.controller;

import com.arjanvanraamsdonk.goodsnext.dto.UserDto;
import com.arjanvanraamsdonk.goodsnext.dto.UserInputDto;
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

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserInputDto userInputDto) {
        UserDto createdUser = userService.addUser(userInputDto);
        return ResponseEntity.ok(createdUser);
    }

//    public ResponseEntity<String> addUser() {
//        return ResponseEntity.ok("POST endpoint is bereikbaar");
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserInputDto userInputDto) {
        UserDto updatedUser = userService.updateUser(id, userInputDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

