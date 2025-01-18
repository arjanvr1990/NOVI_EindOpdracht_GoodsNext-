package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        // Log de principal om te controleren wat beschikbaar is
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal);

        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto, @RequestBody(required = false) ContactInfoDto contactInfoDto) {
        UserDto createdUser = userService.createUser(userDto, contactInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

